package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.rakbow.kureakurusu.dao.ItemMapper;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.dto.*;
import com.rakbow.kureakurusu.data.enums.*;
import com.rakbow.kureakurusu.data.entity.Relation;
import com.rakbow.kureakurusu.data.entity.item.Item;
import com.rakbow.kureakurusu.data.entity.item.SuperItem;
import com.rakbow.kureakurusu.data.vo.EntityRelatedCount;
import com.rakbow.kureakurusu.data.vo.item.*;
import com.rakbow.kureakurusu.exception.ErrorFactory;
import com.rakbow.kureakurusu.toolkit.*;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    private final ItemExtraService extSrv;
    private final ChangelogService logSrv;

    private final PopularUtil popularUtil;
    private final EntityUtil entityUtil;

    private final ItemMapper mapper;
    private final Converter converter;

    private static final EntityType ENTITY_TYPE = EntityType.ITEM;

    //region basic

    @Transactional
    @SneakyThrows
    public long create(ItemSuperCreateDTO dto, MultipartFile[] images) {
        ItemCreateDTO itemDTO = dto.getItem();
        //save item
        Item item = converter.convert(itemDTO, Item.class);
        save(item);
        //save related entities
        relationSrv.batchCreate(ENTITY_TYPE.getValue(), item.getId(), dto.getItem().getType(), dto.getRelatedEntries());
        //save image
        IntStream.range(0, dto.getImages().size()).forEach(i -> dto.getImages().get(i).setFile(images[i]));
        imageSrv.upload(ENTITY_TYPE.getValue(), item.getId(), dto.getImages(), dto.getGenerateThumb());

        //save episode
        if (itemDTO.getType().intValue() == ItemType.ALBUM.getValue()) {
            if (!((AlbumCreateDTO) itemDTO).getDisc().getTracks().isEmpty()) {
                ((AlbumCreateDTO) itemDTO).getDisc().setItemId(item.getId());
                DiscCreateDTO disc = ((AlbumCreateDTO) itemDTO).getDisc();
                extSrv.albumTrackQuickCreate(disc, false);
            }
        }

        logSrv.create(ENTITY_TYPE.getValue(), item.getId(), ChangelogField.DEFAULT, ChangelogOperate.CREATE);

        return item.getId();
    }

    @Transactional
    @SneakyThrows
    public void update(ItemUpdateDTO dto) {
        Item item = converter.convert(dto, Item.class);
        updateById(item);
        logSrv.create(ENTITY_TYPE.getValue(), item.getId(), ChangelogField.BASIC, ChangelogOperate.UPDATE);
    }

    @Transactional
    @SneakyThrows
    public void delete(List<Long> ids) {
        remove(new LambdaQueryWrapper<Item>().in(Item::getId, ids));
    }

    //endregion

    //region query

    @SneakyThrows
    @Transactional(readOnly = true)
    public ItemDetailVO detail(long id) {
        Item item = getById(id);
        if (item == null) throw ErrorFactory.itemNull();
        ItemVO vo = converter.convert(item, ItemVO.class);
        vo.setSpec(ItemUtil.generateSpec(vo.getWidth(), vo.getLength(), vo.getHeight(), vo.getWeight()));

        //update entity popularity
        popularUtil.updateEntityPopularity(ENTITY_TYPE.getValue(), id);

        return ItemDetailVO.builder()
                .item(vo)
                .traffic(entityUtil.buildTraffic(ENTITY_TYPE.getValue(), id))
                .cover(imageSrv.getCache(ENTITY_TYPE.getValue(), item.getId(), ImageType.MAIN))
                .build();
    }

    @SneakyThrows
    @Transactional(readOnly = true)
    public SearchResult<ItemMiniVO> search(ItemSearchQueryDTO dto) {
        // Page<ItemSimpleVO> page = new Page<>(dto.getPage(), dto.getSize(), !dto.allSearch());
        Page<ItemSimpleVO> page = new Page<>(dto.getPage(), dto.getSize());
        MPJLambdaWrapper<Item> wrapper = new MPJLambdaWrapper<Item>()
                .selectAsClass(Item.class, ItemSimpleVO.class)
                .like(StringUtils.isNotBlank(dto.getKeyword()), Item::getName, dto.getKeyword())
                .eq(ObjectUtils.isNotEmpty(dto.getType()), Item::getType, dto.getType())
                .eq(ObjectUtils.isNotEmpty(dto.getSubType()), Item::getSubType, dto.getSubType())
                .eq(ObjectUtils.isNotEmpty(dto.getReleaseType()), Item::getReleaseType, dto.getReleaseType())
                .eq(StringUtils.isNotBlank(dto.getRegion()), Item::getRegion, dto.getRegion())
                .eq(StringUtils.isNotBlank(dto.getBarcode()), Item::getBarcode, dto.getBarcode())
                .eq(StringUtils.isNotBlank(dto.getCatalogId()), Item::getCatalogId, dto.getCatalogId())
                .eq(Item::getStatus, 1)
                .orderBy(dto.isSort(), dto.asc(), dto.getSortField())
                .orderByDesc(!dto.isSort(), Item::getId);
        if (dto.hasRelatedEntries()) {
            //inner join relation
            wrapper.innerJoin(Relation.class, on -> on
                            .eq(Relation::getEntityId, Item::getId)
                            .eq(Relation::getEntityType, ENTITY_TYPE.getValue())
                    )
                    .and(aw -> aw.or(w -> w
                            .eq(Relation::getRelatedEntityType, EntityType.ENTRY.getValue())
                            .in(Relation::getRelatedEntityId, dto.getEntries())))
                    .groupBy(Item::getId)
                    .having(STR."COUNT(t1.related_entity_type) = \{dto.getEntries().size()}");
        }
        IPage<ItemSimpleVO> pages = mapper.selectJoinPage(page, ItemSimpleVO.class, wrapper);
        if (pages.getRecords().isEmpty()) return new SearchResult<>();
        // if (dto.allSearch()) pages.setTotal(entityUtil.getEntityTotalCache(ENTITY_TYPE));
        List<ItemMiniVO> items = new ArrayList<>(converter.convert(pages.getRecords(), ItemMiniVO.class));
        //get image cache
        items.forEach(i -> {
            i.setCover(imageSrv.getCache(ENTITY_TYPE.getValue(), i.getId(), ImageType.MAIN));
            i.setThumb(imageSrv.getCache(ENTITY_TYPE.getValue(), i.getId(), ImageType.THUMB));
        });
        return new SearchResult<>(items, pages.getTotal());
    }

    @SneakyThrows
    @Transactional(readOnly = true)
    public SearchResult<? extends ItemListVO> list(ItemListQueryDTO param) {
        param.init();
        Class<? extends SuperItem> superClass = ItemUtil.getSuperItem(param.getType());
        Class<? extends ItemListVO> itemListVOClass = ItemUtil.getItemListVO(param.getType());

        List<String> columns = ItemUtil.getAllFields(superClass).stream()
                .map(Field::getName)
                .map(CommonUtil::camelToUnderline)
                .distinct()
                .toList();

        MPJLambdaWrapper<Item> wrapper = new MPJLambdaWrapper<Item>()
                .select(columns.toArray(new String[0]))
                .eq(Item::getType, param.getType())
                .and(StringUtils.isNotEmpty(param.getKeyword()),
                        w -> w.or(i -> i.like(Item::getName, param.getKeyword())
                                .or().like("JSON_UNQUOTE(JSON_EXTRACT(aliases, '$[*]'))", STR."%\{param.getKeyword()}%")
                                .or().like(Item::getBarcode, param.getKeyword())
                                .or().like(Item::getCatalogId, param.getKeyword())
                        ))
                .orderBy(param.isSort(), param.asc(), param.getSortField())
                .orderByDesc(!param.isSort(), Item::getId);
        //private query column to sql
        MyBatisUtil.itemListQueryWrapper(param, wrapper);
        IPage<? extends SuperItem> page = mapper.selectJoinPage(new Page<>(param.getPage(), param.getSize()), superClass, wrapper);
        List<? extends ItemListVO> items = converter.convert(page.getRecords(), itemListVOClass);

        //get related resource count
        getItemResourceCount(items);

        return new SearchResult<>(items, page.getTotal());
    }

    @SneakyThrows
    @Transactional(readOnly = true)
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

}