package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.rakbow.kureakurusu.dao.ImageMapper;
import com.rakbow.kureakurusu.dao.ItemMapper;
import com.rakbow.kureakurusu.dao.RelationMapper;
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
import com.rakbow.kureakurusu.toolkit.file.CommonImageUtil;
import com.rakbow.kureakurusu.toolkit.file.QiniuImageUtil;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    //region inject
    private final ResourceService resourceSrv;
    private final RelationService relationSrv;
    private final AlbumService albumSrv;

    private final RedisUtil redisUtil;
    private final PopularUtil popularUtil;
    private final QiniuImageUtil qiniuImageUtil;
    private final VisitUtil visitUtil;
    private final EntityUtil entityUtil;
    private final ItemMapper mapper;
    private final ImageMapper imageMapper;
    private final RelationMapper relationMapper;
    private final Converter converter;
    private final EntityType ENTITY_TYPE = EntityType.ITEM;
    //endregion

    //region static const
    private static final String SUB_T_PREFIX = "t1";
    private static final String SUB_T_CONDITION = " t1 on t1.id = t.id";

    //endregion

    //region basic

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
    public long create(ItemCreateDTO dto) {
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
        List<Image> images = imageMapper.selectList(
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

        relationMapper.delete(
                new LambdaQueryWrapper<Relation>()
                        .eq(Relation::getEntityType, ENTITY_TYPE.getValue())
                        .in(Relation::getEntityId, ids)
        );
    }

    //endregion

    //region query
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
                .cover(resourceSrv.getEntityImageCache(ENTITY_TYPE.getValue(), item.getId(), ImageType.MAIN))
                .build();
    }

    @Transactional
    public SearchResult<ItemMiniVO> search(ItemSearchParams param) {
        IPage<Item> pages;
        Page<Item> page = new Page<>(param.getPage(), param.getSize());
        // 记录开始时间
        long start = System.currentTimeMillis();
        if (param.hasRelatedEntry()) {
            //inner join relation
            MPJLambdaWrapper<Item> wrapper = new MPJLambdaWrapper<Item>()
                    .selectAll(Item.class)
                    .innerJoin(Relation.class, Relation::getEntityId, Item::getId)
                    .eq(Relation::getEntityType, ENTITY_TYPE.getValue())
                    .and(aw -> {
                        for (Long entryId : param.getEntries()) {
                            aw.or(w -> w
                                    .eq(Relation::getRelatedEntityType, EntityType.ENTRY.getValue())
                                    .eq(Relation::getRelatedEntityId, entryId));
                        }
                    })
                    .like(StringUtils.isNotBlank(param.getKeyword()), Item::getName, param.getKeyword())
                    .eq(param.getType() != null, Item::getType, param.getType())
                    .eq(param.getSubType() != null, Item::getSubType, param.getSubType())
                    .eq(param.getReleaseType() != null, Item::getReleaseType, param.getReleaseType())
                    .eq(StringUtils.isNotBlank(param.getRegion()), Item::getRegion, param.getRegion())
                    .eq(StringUtils.isNotBlank(param.getBarcode()), Item::getBarcode, param.getBarcode())
                    .eq(StringUtils.isNotBlank(param.getCatalogId()), Item::getCatalogId, param.getCatalogId())
                    .eq(param.getBonus() != null, Item::getBonus, param.getBonus())
                    .orderBy(param.isSort(), param.asc(), CommonUtil.camelToUnderline(param.getSortField()))
                    .orderByDesc(!param.isSort(), Item::getReleaseDate)
                    .groupBy(Item::getId)
                    .having(STR."COUNT(\{SUB_T_PREFIX}.related_entity_type) = \{param.getEntries().size()}");
            pages = mapper.selectJoinPage(page, Item.class, wrapper);
        } else {
            if (param.isAllSearch()) {
                page = new Page<>(param.getPage(), param.getSize(), false);
            }
            pages = page(
                    page,
                    new LambdaQueryWrapper<Item>()
                            .like(StringUtils.isNotBlank(param.getKeyword()), Item::getName, param.getKeyword())
                            .eq(param.getType() != null, Item::getType, param.getType())
                            .eq(param.getSubType() != null, Item::getSubType, param.getSubType())
                            .eq(param.getReleaseType() != null, Item::getReleaseType, param.getReleaseType())
                            .eq(StringUtils.isNotBlank(param.getRegion()), Item::getRegion, param.getRegion())
                            .eq(StringUtils.isNotBlank(param.getBarcode()), Item::getBarcode, param.getBarcode())
                            .eq(StringUtils.isNotBlank(param.getCatalogId()), Item::getCatalogId, param.getCatalogId())
                            .eq(param.getBonus() != null, Item::getBonus, param.getBonus())
                            .orderByDesc(!param.isSort(), Item::getId)
            );
            if (param.isAllSearch()) {
                pages.setTotal(entityUtil.getEntityTotalCache(ENTITY_TYPE));
            }
        }
        if (pages.getRecords().isEmpty())
            return new SearchResult<>();
        List<ItemMiniVO> items = new ArrayList<>(converter.convert(pages.getRecords(), ItemMiniVO.class));
        //get image cache
        items.forEach(i -> {
            i.setCover(resourceSrv.getEntityImageCache(ENTITY_TYPE.getValue(), i.getId(), ImageType.MAIN));
            i.setThumb(resourceSrv.getEntityImageCache(ENTITY_TYPE.getValue(), i.getId(), ImageType.THUMB));
        });
        return new SearchResult<>(items, pages.getTotal(), pages.getCurrent(), pages.getSize(),
                String.format("%.2f", (System.currentTimeMillis() - start) / 1000.0));
    }

    @Transactional
    @SneakyThrows
    public SearchResult<? extends ItemListVO> list(ListQuery dto) {

        ItemListQueryDTO param = ItemUtil.getItemListQueryDTO(dto);

        Class<? extends SuperItem> superClass = ItemUtil.getSuperItem(param.getType());
        Class<? extends SubItem> subClass = ItemUtil.getSubClass(param.getType());
        Class<? extends ItemListVO> itemListVOClass = ItemUtil.getItemListVO(param.getType());

        MPJLambdaWrapper<Item> wrapper = new MPJLambdaWrapper<Item>()
                .selectAll(Item.class)
                .selectAll(subClass, SUB_T_PREFIX)
                .leftJoin(STR."\{MyBatisUtil.getTableName(subClass)}\{SUB_T_CONDITION}")
                .eq(Item::getType, param.getType())
                .like(StringUtils.isNotEmpty(param.getName()), Item::getName, param.getName())
                .like(StringUtils.isNotEmpty(param.getAliases()),
                        "JSON_UNQUOTE(JSON_EXTRACT(aliases, '$[*]'))", STR."%\{param.getAliases()}%")
                .eq(StringUtils.isNotEmpty(param.getRegion()), Item::getRegion, param.getRegion())
                .like(StringUtils.isNotEmpty(param.getBarcode()), Item::getBarcode, param.getBarcode())
                .like(StringUtils.isNotEmpty(param.getCatalogId()), Item::getCatalogId, param.getCatalogId())
                .eq(param.getReleaseType() != null, Item::getReleaseType, param.getReleaseType())
                .eq(param.getBonus() != null, Item::getBonus, Boolean.TRUE.equals(param.getBonus()) ? 1 : 0)
                .orderBy(param.isSort(), param.asc(), CommonUtil.camelToUnderline(param.getSortField()));
        //private query column to sql
        MyBatisUtil.itemListQueryWrapper(param, wrapper);

        IPage<? extends SuperItem> page = mapper.selectJoinPage(new Page<>(param.getPage(), param.getSize()), superClass, wrapper);

        List<? extends ItemListVO> items = converter.convert(page.getRecords(), itemListVOClass);

        //get related resource count
        getItemResourceCount(items);

        return new SearchResult<>(items, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Transactional
    @SneakyThrows
    public void getItemResourceCount(List<? extends ItemListVO> items) {
        if(items.isEmpty()) return;
        List<Long> ids = items.stream().map(ItemListVO::getId).toList();
        int entityType = ENTITY_TYPE.getValue();
        // 将资源统计列表转换为 Map<entityId, count>
        Map<Long, Integer> fileCountMap = resourceSrv.getFileCount(entityType, ids).stream()
                .collect(Collectors.toMap(EntityRelatedCount::getEntityId, EntityRelatedCount::getCount));
        Map<Long, Integer> imageCountMap = resourceSrv.getImageCount(entityType, ids).stream()
                .collect(Collectors.toMap(EntityRelatedCount::getEntityId, EntityRelatedCount::getCount));
        for (ItemListVO item : items) {
            long id = item.getId();
            item.setFileCount(fileCountMap.getOrDefault(id, 0));
            item.setImageCount(imageCountMap.getOrDefault(id, 0));
        }
    }

    //endregion

    @SneakyThrows
    public ItemTypeRelation getItemTypeRelation(long id) {
        String redisKey = STR."item_type_related:\{id}";
        if (!redisUtil.hasKey(redisKey)) return null;
        return redisUtil.get(redisKey, ItemTypeRelation.class);
    }

    @Transactional
    @SneakyThrows
    public long advanceCreate(ItemCreateDTO item, List<ImageMiniDTO> images,
                              List<RelatedEntityMiniDTO> relatedEntities, boolean generateThumb) {
        //save item
        long id = create(item);
        //save related entities
        relationSrv.batchCreate(ENTITY_TYPE.getValue(), id, relatedEntities);
        //save image
        images.forEach(i -> i.setFile(CommonImageUtil.base64ToMultipartFile(i.getBase64Code())));
        resourceSrv.uploadEntityImage(ENTITY_TYPE.getValue(), id, images, generateThumb);

        //save episode
        if (item.getType().intValue() == ItemType.ALBUM.getValue()) {
            albumSrv.quickCreateAlbumTrack(id, ((AlbumCreateDTO) item).getTrackList(), false);
        }

        return id;
    }

}