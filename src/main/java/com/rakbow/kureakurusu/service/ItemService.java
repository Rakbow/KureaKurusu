package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.rakbow.kureakurusu.annotation.Search;
import com.rakbow.kureakurusu.dao.ItemMapper;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.dto.*;
import com.rakbow.kureakurusu.data.entity.FavListItem;
import com.rakbow.kureakurusu.data.entity.Relation;
import com.rakbow.kureakurusu.data.entity.item.Item;
import com.rakbow.kureakurusu.data.entity.item.SuperItem;
import com.rakbow.kureakurusu.data.enums.EntityType;
import com.rakbow.kureakurusu.data.enums.ImageType;
import com.rakbow.kureakurusu.data.enums.ItemType;
import com.rakbow.kureakurusu.data.vo.EntityRelatedCount;
import com.rakbow.kureakurusu.data.vo.item.*;
import com.rakbow.kureakurusu.exception.EntityNullException;
import com.rakbow.kureakurusu.toolkit.*;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Rakbow
 * @since 2024/4/11 4:07
 */
@Service
@RequiredArgsConstructor
public class ItemService extends ServiceImpl<ItemMapper, Item> {

    private final ImageService imgSrv;
    private final FileService fileSrv;
    private final RelationService relSrv;
    private final ResourceService resSrv;
    private final ItemExtraService extSrv;

    private final PopularUtil popularUtil;
    private final EntityUtil entityUtil;

    private final ItemMapper mapper;
    private final Converter converter;

    private static final EntityType ENTITY_TYPE = EntityType.ITEM;

    //region basic

    @Transactional
    @SneakyThrows
    public long create(ItemSuperCreateDTO dto, MultipartFile[] images) {
        ItemCreateDTO itemDTO = dto.item();
        //save item
        Item item = converter.convert(itemDTO, Item.class);
        save(item);
        //save related entities
        relSrv.batchCreate(ENTITY_TYPE.getValue(), item.getId(), itemDTO.getType(), dto.relatedEntries());
        //save image
        IntStream.range(0, dto.images().size()).forEach(i -> dto.images().get(i).setFile(images[i]));
        imgSrv.upload(ENTITY_TYPE.getValue(), item.getId(), dto.images(), dto.generateThumb());

        //save episode
        if (itemDTO.getType().intValue() == ItemType.ALBUM.getValue()) {
            if (!((AlbumCreateDTO) itemDTO).getDisc().getTracks().isEmpty()) {
                ((AlbumCreateDTO) itemDTO).getDisc().setItemId(item.getId());
                DiscCreateDTO disc = ((AlbumCreateDTO) itemDTO).getDisc();
                extSrv.albumTrackQuickCreate(disc, false);
            }
        }

        // logSrv.create(ENTITY_TYPE.getValue(), item.getId(), ChangelogField.DEFAULT, ChangelogOperate.CREATE);

        return item.getId();
    }

    @Transactional
    @SneakyThrows
    public void update(ItemUpdateDTO dto) {
        Item item = converter.convert(dto, Item.class);
        updateById(item);
        // logSrv.create(ENTITY_TYPE.getValue(), item.getId(), ChangelogField.BASIC, ChangelogOperate.UPDATE);
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
        if (item == null) throw new EntityNullException();
        ItemVO vo = converter.convert(item, ItemVO.class);
        vo.setSpec(ItemUtil.generateSpec(vo.getWidth(), vo.getLength(), vo.getHeight(), vo.getWeight()));

        //update entity popularity
        popularUtil.updateEntityPopularity(ENTITY_TYPE.getValue(), id);

        return ItemDetailVO.builder()
                .item(vo)
                .traffic(entityUtil.buildTraffic(ENTITY_TYPE.getValue(), id))
                .cover(imgSrv.getCache(ENTITY_TYPE.getValue(), item.getId(), ImageType.MAIN))
                .build();
    }

    @SneakyThrows
    @Transactional(readOnly = true)
    @Search
    public SearchResult<ItemSearchVO> search(ItemSearchQueryDTO dto) {
        Page<ItemSimpleVO> page = new Page<>(dto.getPage(), dto.getSize());
        MPJLambdaWrapper<Item> wrapper = new MPJLambdaWrapper<>() {{
            selectAsClass(Item.class, ItemSimpleVO.class);
            like(StringUtil.isNotBlank(dto.getKeyword()), Item::getName, dto.getKeyword());
            likeRight(StringUtil.isNotBlank(dto.getBarcode()), Item::getBarcode, dto.getBarcode());
            likeRight(StringUtil.isNotBlank(dto.getCatalogId()), Item::getCatalogId, dto.getCatalogId());
            eq(Objects.nonNull(dto.getType()), Item::getType, dto.getType());
            eq(Objects.nonNull(dto.getSubType()), Item::getSubType, dto.getSubType());
            eq(Objects.nonNull(dto.getReleaseType()), Item::getReleaseType, dto.getReleaseType());
            eq(StringUtil.isNotBlank(dto.getRegion()), Item::getRegion, dto.getRegion());
            eq(Item::getStatus, 1);
            orderBy(!(Objects.nonNull(dto.getListId()) && StringUtil.equals(dto.getSortField(), "id"))
                            && dto.isSort(), dto.asc(), dto.getSortField());
            orderByDesc(!dto.isSort(), Item::getId);
        }};
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
        if (Objects.nonNull(dto.getListId())) {
            wrapper.innerJoin(FavListItem.class, on ->
                            on.eq(FavListItem::getEntityId, Item::getId))
                    .and(aw -> aw.and(w ->
                            w.eq(FavListItem::getListId, dto.getListId())));
            if (dto.isSort() && StringUtil.equals(dto.getSortField(), "id")) {
                if (dto.asc()) {
                    wrapper.orderByAsc(FavListItem::getCreatedAt);
                } else {
                    wrapper.orderByDesc(FavListItem::getCreatedAt);
                }
            }
        }
        IPage<ItemSimpleVO> pages = mapper.selectJoinPage(page, ItemSimpleVO.class, wrapper);
        if (pages.getRecords().isEmpty()) return new SearchResult<>();
        List<ItemSearchVO> items = new ArrayList<>(converter.convert(pages.getRecords(), ItemSearchVO.class));
        //get image cache
        items.forEach(i -> {
            i.setCover(imgSrv.getCache(ENTITY_TYPE.getValue(), i.getId(), ImageType.MAIN));
            i.setThumb(imgSrv.getCache(ENTITY_TYPE.getValue(), i.getId(), ImageType.THUMB));
        });
        if (Objects.nonNull(dto.getListId())) {
            resSrv.getLocalResourceCompletedFlag(items);
        }
        return new SearchResult<>(items, pages.getTotal());
    }

    @SneakyThrows
    @Transactional(readOnly = true)
    @Search
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
                .and(StringUtil.isNotEmpty(param.getKeyword()),
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
        Map<Long, Integer> imageCountMap = imgSrv.count(entityType, ids).stream()
                .collect(Collectors.toMap(EntityRelatedCount::getEntityId, EntityRelatedCount::getCount));
        for (ItemListVO item : items) {
            item.setFileCount(fileCountMap.getOrDefault(item.getId(), 0));
            item.setImageCount(imageCountMap.getOrDefault(item.getId(), 0));
        }
    }

    //endregion

}