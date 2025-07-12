package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.rakbow.kureakurusu.dao.ItemMapper;
import com.rakbow.kureakurusu.data.ItemTypeRelation;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.dto.*;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.emun.ImageType;
import com.rakbow.kureakurusu.data.emun.ItemType;
import com.rakbow.kureakurusu.data.entity.Relation;
import com.rakbow.kureakurusu.data.entity.item.Item;
import com.rakbow.kureakurusu.data.entity.item.SubItem;
import com.rakbow.kureakurusu.data.entity.item.SuperItem;
import com.rakbow.kureakurusu.data.entity.resource.Image;
import com.rakbow.kureakurusu.data.vo.EntityRelatedCount;
import com.rakbow.kureakurusu.data.vo.item.ItemDetailVO;
import com.rakbow.kureakurusu.data.vo.item.ItemListVO;
import com.rakbow.kureakurusu.data.vo.item.ItemMiniVO;
import com.rakbow.kureakurusu.data.vo.item.ItemVO;
import com.rakbow.kureakurusu.exception.ErrorFactory;
import com.rakbow.kureakurusu.service.item.AlbumService;
import com.rakbow.kureakurusu.toolkit.*;
import com.rakbow.kureakurusu.toolkit.file.QiniuImageUtil;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Rakbow
 * @since 2024/4/11 4:07
 */
@Service
@RequiredArgsConstructor
public class ItemService extends ServiceImpl<ItemMapper, Item> {

    private final ImageService imageSrv;
    private final FileService fileSrv;
    private final RelationService relationSrv;
    private final AlbumService albumSrv;

    private final RedisUtil redisUtil;
    private final PopularUtil popularUtil;
    private final QiniuImageUtil qiniuImageUtil;
    private final VisitUtil visitUtil;
    private final EntityUtil entityUtil;

    private final ItemMapper mapper;
    private final Converter converter;

    private static final EntityType ENTITY_TYPE = EntityType.ITEM;
    private static final String SUB_T_PREFIX = "t1";
    private static final String SUB_T_CONDITION = " t1 on t1.id = t.id";

    //region basic

    @Transactional
    @SneakyThrows
    public long create(ItemSuperCreateDTO dto, MultipartFile[] images) {
        ItemCreateDTO item = dto.getItem();
        //save item
        long id = insert(item);
        //save related entities
        relationSrv.batchCreate(ENTITY_TYPE.getValue(), id, dto.getItem().getType(), dto.getRelatedEntities());
        //save image
        for (int i = 0; i < dto.getImages().size(); i++) {
            dto.getImages().get(i).setFile(images[i]);
        }
        imageSrv.upload(ENTITY_TYPE.getValue(), id, dto.getImages(), dto.getGenerateThumb());

        //save episode
        if (item.getType().intValue() == ItemType.ALBUM.getValue()) {
            if (!((AlbumCreateDTO) item).getDisc().getTracks().isEmpty()) {
                ((AlbumCreateDTO) item).getDisc().setItemId(id);
                AlbumDiscCreateDTO disc = ((AlbumCreateDTO) item).getDisc();
                albumSrv.quickCreateAlbumTrack(disc, false);
            }
        }

        return id;
    }

    @Transactional
    @SneakyThrows
    public long insert(ItemCreateDTO dto) {
        Class<? extends SubItem> subClass = ItemUtil.getSubClass(dto.getType());
        BaseMapper<SubItem> subMapper = MyBatisUtil.getMapper(subClass);

        Item item = converter.convert(dto, Item.class);
        SubItem subItem = converter.convert(dto, subClass);

        save(item);
        subItem.setId(item.getId());
        subMapper.insert(subItem);

        //add redis ItemTypeRelation key
        ItemTypeRelation relation = new ItemTypeRelation(item);
        String key = STR."item_type_related:\{item.getId()}";
        redisUtil.set(key, relation);

        return item.getId();
    }

    @Transactional
    @SneakyThrows
    public void update(ItemUpdateDTO dto) {

        Class<? extends SubItem> subClass = ItemUtil.getSubClass(dto.getType());
        BaseMapper<SubItem> subMapper = MyBatisUtil.getMapper(subClass);

        Item item = converter.convert(dto, Item.class);
        SubItem subItem = converter.convert(dto, subClass);

        updateById(item);
        subMapper.updateById(subItem);
    }

    @Transactional
    @SneakyThrows
    public void delete(List<Long> ids) {
        //get original data
        List<Item> items = listByIds(ids);
        if (items.isEmpty()) return;
        List<Image> images = imageSrv.list(
                new LambdaQueryWrapper<Image>()
                        .eq(Image::getEntityType, ENTITY_TYPE)
                        .in(Image::getEntityId, ids)
        );
        for (Item item : items) {
            //delete all image
            qiniuImageUtil.deleteAllImage(images);
            //delete visit record
            visitUtil.del(ENTITY_TYPE.getValue(), item.getId());
        }
        int type = items.getFirst().getType().getValue();
        Class<? extends SubItem> subClass = ItemUtil.getSubClass(type);
        BaseMapper<SubItem> subMapper = MyBatisUtil.getMapper(subClass);

        remove(new LambdaQueryWrapper<Item>().in(Item::getId, ids));
        subMapper.delete(new LambdaQueryWrapper<SubItem>().in(SubItem::getId, ids));

        relationSrv.remove(
                new LambdaQueryWrapper<Relation>()
                        .eq(Relation::getEntityType, ENTITY_TYPE.getValue())
                        .in(Relation::getEntityId, ids)
        );
    }

    //endregion

    //region query

    @Transactional
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public <T extends SuperItem> T getById(long id) {
        ItemTypeRelation relation = getItemTypeRelation(id);
        if (relation == null) return null;

        Class<? extends SubItem> s = ItemUtil.getSubClass(relation.getType());
        Class<? extends SuperItem> t = ItemUtil.getSuperItem(relation.getType());
        MPJLambdaWrapper<Item> wrapper = new MPJLambdaWrapper<Item>()
                .selectAll(Item.class)
                .selectAll(s, SUB_T_PREFIX)
                .leftJoin(STR."\{MyBatisUtil.getTableName(s)}\{SUB_T_CONDITION}")
                .eq(Item::getId, id);
        return (T) mapper.selectJoinOne(t, wrapper);
    }

    @Transactional
    @SneakyThrows
    public ItemDetailVO detail(long id) {
        SuperItem item = getById(id);
        if (item == null) throw ErrorFactory.itemNull();
        Class<? extends ItemVO> targetVOClass = ItemUtil.getDetailVO(item.getType().getValue());
        ItemVO vo = converter.convert(item, targetVOClass);
        vo.setSpec(ItemUtil.generateSpec(vo.getWidth(), vo.getLength(), vo.getHeight(), vo.getWeight()));

        //update entity popularity
        popularUtil.updateEntityPopularity(ENTITY_TYPE.getValue(), id);

        return ItemDetailVO.builder()
                .type(item.getType().getValue())
                .item(vo)
                .traffic(entityUtil.buildTraffic(ENTITY_TYPE.getValue(), id))
                .cover(imageSrv.getCache(ENTITY_TYPE.getValue(), item.getId(), ImageType.MAIN))
                .build();
    }

    @Transactional
    public SearchResult<ItemMiniVO> search(ItemSearchQueryDTO dto) {
        dto.init();
        IPage<Item> pages;
        Page<Item> page = new Page<>(dto.getPage(), dto.getSize(), !dto.allSearch());
        MPJLambdaWrapper<Item> wrapper;
        long start = System.currentTimeMillis();
        if (dto.hasRelatedEntries()) {
            //inner join relation
            wrapper = new MPJLambdaWrapper<Item>()
                    .selectAll(Item.class)
                    .innerJoin(Relation.class, on -> on
                            .eq(Relation::getEntityId, Item::getId)
                            .eq(Relation::getEntityType, ENTITY_TYPE.getValue())
                    )
                    .and(aw -> aw.or(w -> w
                            .eq(Relation::getRelatedEntityType, EntityType.ENTRY.getValue())
                            .in(Relation::getRelatedEntityId, dto.getEntries())))
                    .like(StringUtils.isNotBlank(dto.getKeyword()), Item::getName, dto.getKeyword())
                    .eq(ObjectUtils.isNotEmpty(dto.getType()), Item::getType, dto.getType())
                    .eq(ObjectUtils.isNotEmpty(dto.getSubType()), Item::getSubType, dto.getSubType())
                    .eq(ObjectUtils.isNotEmpty(dto.getReleaseType()), Item::getReleaseType, dto.getReleaseType())
                    .eq(StringUtils.isNotBlank(dto.getRegion()), Item::getRegion, dto.getRegion())
                    .eq(StringUtils.isNotBlank(dto.getBarcode()), Item::getBarcode, dto.getBarcode())
                    .eq(StringUtils.isNotBlank(dto.getCatalogId()), Item::getCatalogId, dto.getCatalogId())
                    .orderBy(dto.isSort(), dto.asc(), CommonUtil.camelToUnderline(dto.getSortField()))
                    .orderByDesc(!dto.isSort(), Item::getReleaseDate)
                    .groupBy(Item::getId)
                    .having(STR."COUNT(\{SUB_T_PREFIX}.related_entity_type) = \{dto.getEntries().size()}");
            pages = mapper.selectJoinPage(page, Item.class, wrapper);
        } else {
            wrapper = new MPJLambdaWrapper<Item>()
                    .like(StringUtils.isNotBlank(dto.getKeyword()), Item::getName, dto.getKeyword())
                    .eq(ObjectUtils.isNotEmpty(dto.getType()), Item::getType, dto.getType())
                    .eq(ObjectUtils.isNotEmpty(dto.getSubType()), Item::getSubType, dto.getSubType())
                    .eq(ObjectUtils.isNotEmpty(dto.getReleaseType()), Item::getReleaseType, dto.getReleaseType())
                    .eq(StringUtils.isNotEmpty(dto.getRegion()), Item::getRegion, dto.getRegion())
                    .eq(StringUtils.isNotEmpty(dto.getBarcode()), Item::getBarcode, dto.getBarcode())
                    .eq(StringUtils.isNotEmpty(dto.getCatalogId()), Item::getCatalogId, dto.getCatalogId())
                    .orderByDesc(!dto.isSort(), Item::getId);
            pages = page(page, wrapper);
        }
        if (pages.getRecords().isEmpty()) return new SearchResult<>();
        if (dto.allSearch()) pages.setTotal(entityUtil.getEntityTotalCache(ENTITY_TYPE));
        List<ItemMiniVO> items = new ArrayList<>(converter.convert(pages.getRecords(), ItemMiniVO.class));
        //get image cache
        items.forEach(i -> {
            i.setCover(imageSrv.getCache(ENTITY_TYPE.getValue(), i.getId(), ImageType.MAIN));
            i.setThumb(imageSrv.getCache(ENTITY_TYPE.getValue(), i.getId(), ImageType.THUMB));
        });
        return new SearchResult<>(items, pages.getTotal(), start);
    }

    @Transactional
    @SneakyThrows
    public SearchResult<? extends ItemListVO> list(ItemListQueryDTO param) {
        param.init();
        Class<? extends SuperItem> superClass = ItemUtil.getSuperItem(param.getType());
        Class<? extends SubItem> subClass = ItemUtil.getSubClass(param.getType());
        Class<? extends ItemListVO> itemListVOClass = ItemUtil.getItemListVO(param.getType());

        MPJLambdaWrapper<Item> wrapper = new MPJLambdaWrapper<Item>()
                .selectAll(Item.class)
                .selectAll(subClass, SUB_T_PREFIX)
                .leftJoin(STR."\{MyBatisUtil.getTableName(subClass)}\{SUB_T_CONDITION}")
                .eq(Item::getType, param.getType())
                .and(StringUtils.isNotEmpty(param.getKeyword()),
                        w -> w.or(i -> i.like(Item::getName, param.getKeyword())
                                .or().like("JSON_UNQUOTE(JSON_EXTRACT(aliases, '$[*]'))", STR."%\{param.getKeyword()}%")
                                .or().like(Item::getBarcode, param.getKeyword())
                                .or().like(Item::getCatalogId, param.getKeyword())
                        ))
                .orderBy(param.isSort(), param.asc(), CommonUtil.camelToUnderline(param.getSortField()))
                .orderByDesc(!param.isSort(), Item::getId);
        //private query column to sql
        MyBatisUtil.itemListQueryWrapper(param, wrapper);
        long start = System.currentTimeMillis();
        IPage<? extends SuperItem> page = mapper.selectJoinPage(new Page<>(param.getPage(), param.getSize()), superClass, wrapper);

        List<? extends ItemListVO> items = converter.convert(page.getRecords(), itemListVOClass);

        //get related resource count
        getItemResourceCount(items);

        return new SearchResult<>(items, page.getTotal(), start);
    }

    @Transactional
    @SneakyThrows
    public void getItemResourceCount(List<? extends ItemListVO> items) {
        if (items.isEmpty()) return;
        List<Long> ids = items.stream().map(ItemListVO::getId).toList();
        int entityType = ENTITY_TYPE.getValue();
        // 将资源统计列表转换为 Map<entityId, count>
        Map<Long, Integer> fileCountMap = fileSrv.count(entityType, ids).stream()
                .collect(Collectors.toMap(EntityRelatedCount::getEntityId, EntityRelatedCount::getCount));
        Map<Long, Integer> imageCountMap = imageSrv.count(entityType, ids).stream()
                .collect(Collectors.toMap(EntityRelatedCount::getEntityId, EntityRelatedCount::getCount));
        for (ItemListVO item : items) {
            item.setFileCount(fileCountMap.getOrDefault(item.getId(), 0));
            item.setImageCount(imageCountMap.getOrDefault(item.getId(), 0));
        }
    }

    //endregion

    @SneakyThrows
    public ItemTypeRelation getItemTypeRelation(long id) {
        String redisKey = STR."item_type_related:\{id}";
        if (!redisUtil.hasKey(redisKey)) return null;
        return redisUtil.get(redisKey, ItemTypeRelation.class);
    }

}