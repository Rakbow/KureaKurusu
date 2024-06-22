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
import com.rakbow.kureakurusu.data.SimpleSearchParam;
import com.rakbow.kureakurusu.data.dto.ItemCreateDTO;
import com.rakbow.kureakurusu.data.dto.ItemListQueryDTO;
import com.rakbow.kureakurusu.data.dto.ItemUpdateDTO;
import com.rakbow.kureakurusu.data.dto.ListQueryDTO;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.entity.Item;
import com.rakbow.kureakurusu.data.entity.Relation;
import com.rakbow.kureakurusu.data.entity.SubItem;
import com.rakbow.kureakurusu.data.entity.common.SuperItem;
import com.rakbow.kureakurusu.data.image.Image;
import com.rakbow.kureakurusu.data.vo.item.ItemDetailVO;
import com.rakbow.kureakurusu.data.vo.item.ItemListVO;
import com.rakbow.kureakurusu.data.vo.item.ItemMiniVO;
import com.rakbow.kureakurusu.data.vo.item.ItemVO;
import com.rakbow.kureakurusu.toolkit.*;
import com.rakbow.kureakurusu.toolkit.file.QiniuImageUtil;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/4/11 4:07
 */
@Service
@RequiredArgsConstructor
public class ItemService extends ServiceImpl<ItemMapper, Item> {

    //region inject
    private final ResourceService resourceSrv;
    private final RedisUtil redisUtil;
    private final QiniuImageUtil qiniuImageUtil;
    private final VisitUtil visitUtil;
    private final EntityUtil entityUtil;
    private final ItemMapper mapper;
    private final ImageMapper imageMapper;
    private final RelationMapper relationMapper;
    private final Converter converter;
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

        Class<? extends SubItem> s = ItemUtil.getSubItem(relation.getType());
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
    public String insert(ItemCreateDTO dto) {
        Class<? extends SubItem> subClass = ItemUtil.getSubItem(dto.getType());
        BaseMapper<SubItem> subMapper = MyBatisUtil.getMapper(subClass);

        Item item = converter.convert(dto, Item.class);
        SubItem subItem = converter.convert(dto, subClass);

        mapper.insert(item);
        subItem.setId(item.getId());
        subMapper.insert(subItem);

        return I18nHelper.getMessage("entity.crud.insert.success");
    }

    @Transactional
    @SneakyThrows
    public String update(ItemUpdateDTO dto) {

        Class<? extends SubItem> subClass = ItemUtil.getSubItem(dto.getType());
        BaseMapper<SubItem> subMapper = MyBatisUtil.getMapper(subClass);

        Item item = converter.convert(dto, Item.class);
        item.setEditedTime(DateHelper.now());
        SubItem subItem = converter.convert(dto, subClass);

        mapper.updateById(item);
        subMapper.updateById(subItem);

        return I18nHelper.getMessage("entity.crud.update.success");
    }

    @Transactional
    @SneakyThrows
    public String delete(List<Long> ids) {
        //get original data
        List<Item> items = mapper.selectBatchIds(ids);
        List<Image> images = imageMapper.selectList(
                new LambdaQueryWrapper<Image>()
                        .eq(Image::getEntityType, EntityType.ITEM)
                        .in(Image::getEntityId, ids)
        );
        if (items.isEmpty()) throw new Exception("");
        for (Item item : items) {
            //delete all image
            qiniuImageUtil.deleteAllImage(images);
            //delete visit record
            visitUtil.del(EntityType.ITEM.getValue(), item.getId());
        }
        int type = items.getFirst().getType().getValue();
        Class<? extends SubItem> subClass = ItemUtil.getSubItem(type);
        BaseMapper<SubItem> subMapper = MyBatisUtil.getMapper(subClass);

        mapper.delete(new LambdaQueryWrapper<Item>().in(Item::getId, ids));
        subMapper.delete(new LambdaQueryWrapper<SubItem>().in(SubItem::getId, ids));

        relationMapper.delete(
                new LambdaQueryWrapper<Relation>()
                        .eq(Relation::getEntityType, EntityType.ITEM.getValue())
                        .in(Relation::getEntityId, ids)
        );

        return I18nHelper.getMessage("entity.curd.delete.success");
    }

    //endregion

    //region query
    @Transactional
    @SneakyThrows
    public ItemDetailVO detail(long id) {
        SuperItem item = getById(id);
        if (item == null) throw new Exception(I18nHelper.getMessage("item.url.error"));
        Class<? extends ItemVO> targetVOClass = ItemUtil.getItemDetailVO(item.getType().getValue());

        return ItemDetailVO.builder()
                .type(item.getType().getValue())
                .item(converter.convert(item, targetVOClass))
                .traffic(entityUtil.getPageTraffic(EntityType.ITEM.getValue(), id))
                .options(ItemUtil.getOptions(item.getType().getValue()))
                .cover(resourceSrv.getItemCover(item.getType(), item.getId()))
                .images(resourceSrv.getDefaultImages(EntityType.ITEM.getValue(), id))
                .imageCount(resourceSrv.getDefaultImagesCount(EntityType.ITEM.getValue(), id))
                .build();
    }

    @Transactional
    public SearchResult<ItemMiniVO> search(SimpleSearchParam param) {
        if (param.keywordEmpty()) new SearchResult<>();

        LambdaQueryWrapper<Item> wrapper = new LambdaQueryWrapper<Item>()
                .or().like(Item::getName, param.getKeyword())
                .and(i -> i.apply("JSON_UNQUOTE(JSON_EXTRACT(aliases, '$[*]')) LIKE concat('%', {0}, '%')",
                        param.getKeyword()))
                .orderByDesc(Item::getId);

        IPage<Item> pages = mapper.selectPage(new Page<>(param.getPage(), param.getSize()), wrapper);

        List<ItemMiniVO> items = converter.convert(pages.getRecords(), ItemMiniVO.class);

        return new SearchResult<>(items, pages.getTotal(), pages.getCurrent(), pages.getSize());
    }

    @Transactional
    @SneakyThrows
    public SearchResult<? extends ItemListVO> list(ListQueryDTO dto) {

        ItemListQueryDTO param = ItemUtil.getItemListQueryDTO(dto);

        Class<? extends SuperItem> superClass = ItemUtil.getSuperItem(param.getType());
        Class<? extends SubItem> subClass = ItemUtil.getSubItem(param.getType());
        Class<? extends ItemListVO> itemListVOClass = ItemUtil.getItemListVO(param.getType());

        MPJLambdaWrapper<Item> wrapper = new MPJLambdaWrapper<Item>()
                .selectAll(Item.class)
                .selectAll(subClass, SUB_T_PREFIX)
                .leftJoin(STR."\{MyBatisUtil.getTableName(subClass)}\{SUB_T_CONDITION}")
                .eq(Item::getType, param.getType())
                .like(StringUtils.isNotBlank(param.getName()), Item::getName, param.getName())
                .like(StringUtils.isNotBlank(param.getAliases()),
                        "JSON_UNQUOTE(JSON_EXTRACT(aliases, '$[*]'))", STR."%\{param.getAliases()}%")
                .eq(StringUtils.isNotBlank(param.getRegion()), Item::getRegion, param.getRegion())
                .like(StringUtils.isNotBlank(param.getBarcode()), Item::getBarcode, param.getBarcode())
                .eq(param.getReleaseType() != null, Item::getReleaseType, param.getReleaseType())
                .eq(param.getBonus() != null, Item::getBonus, Boolean.TRUE.equals(param.getBonus()) ? 1 : 0)
                .orderBy(param.isSort(), param.asc(), CommonUtil.camelToUnderline(param.getSortField()));
        //private query column to sql
        MyBatisUtil.itemListQueryWrapper(param, wrapper);

        IPage<? extends SuperItem> page = mapper.selectJoinPage(new Page<>(param.getPage(), param.getSize()), superClass, wrapper);

        List<? extends ItemListVO> items = converter.convert(page.getRecords(), itemListVOClass);

        return new SearchResult<>(items, page.getTotal(), page.getCurrent(), page.getSize());
    }

    //endregion

    @SneakyThrows
    public ItemTypeRelation getItemTypeRelation(long id) {
        String redisKey = STR."item_type_related:\{id}";
        if (!redisUtil.hasKey(redisKey)) return null;
        return redisUtil.get(redisKey, ItemTypeRelation.class);
    }

}