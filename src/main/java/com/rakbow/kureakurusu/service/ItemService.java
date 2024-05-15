package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.rakbow.kureakurusu.annotation.UniqueVisitor;
import com.rakbow.kureakurusu.dao.ItemMapper;
import com.rakbow.kureakurusu.dao.PersonRelationMapper;
import com.rakbow.kureakurusu.data.ItemTypeRelation;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.SimpleSearchParam;
import com.rakbow.kureakurusu.data.dto.ItemCreateDTO;
import com.rakbow.kureakurusu.data.dto.ItemListQueryDTO;
import com.rakbow.kureakurusu.data.dto.ItemUpdateDTO;
import com.rakbow.kureakurusu.data.dto.ListQueryDTO;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.entity.Item;
import com.rakbow.kureakurusu.data.entity.PersonRelation;
import com.rakbow.kureakurusu.data.entity.SubItem;
import com.rakbow.kureakurusu.data.entity.common.SuperItem;
import com.rakbow.kureakurusu.data.vo.item.ItemDetailVO;
import com.rakbow.kureakurusu.data.vo.item.ItemVO;
import com.rakbow.kureakurusu.data.vo.item.ItemListVO;
import com.rakbow.kureakurusu.data.vo.item.ItemMiniVO;
import com.rakbow.kureakurusu.toolkit.*;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
import com.rakbow.kureakurusu.toolkit.file.CommonImageUtil;
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

    private final PersonService personSrv;

    private final RedisUtil redisUtil;
    private final QiniuImageUtil qiniuImageUtil;
    private final VisitUtil visitUtil;
    private final EntityUtil entityUtil;

    private final ItemMapper mapper;

    private final PersonRelationMapper relationMapper;

    private final Converter converter;

    //region basic

    @Transactional
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public <T extends SuperItem> T getById(long id) {
        ItemTypeRelation relation = getItemTypeRelation(id);
        if (relation == null) return null;

        Class<? extends SubItem> sourceClass = ItemUtil.getSubItem(relation.getType());
        Class<? extends SuperItem> targetClass = ItemUtil.getSuperItem(relation.getType());
        MPJLambdaWrapper<Item> wrapper = new MPJLambdaWrapper<Item>()
                .selectAll(Item.class)
                .selectAll(sourceClass, "t1")
                .leftJoin(STR."\{MyBatisUtil.getTableName(sourceClass)} t1 on t1.id = t.id")
                .eq(Item::getId, id);
        return (T) mapper.selectJoinOne(targetClass, wrapper);
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
        if (items.isEmpty()) throw new Exception("");
        for (Item item : items) {
            //delete all image
            qiniuImageUtil.deleteAllImage(item.getImages());
            //delete visit record
            visitUtil.deleteVisit(EntityType.ITEM.getValue(), item.getId());
        }
        int type = items.getFirst().getType().getValue();
        Class<? extends SubItem> subClass = ItemUtil.getSubItem(type);
        BaseMapper<SubItem> subMapper = MyBatisUtil.getMapper(subClass);

        mapper.delete(new LambdaQueryWrapper<Item>().in(Item::getId, ids));
        subMapper.delete(new LambdaQueryWrapper<SubItem>().in(SubItem::getId, ids));

        relationMapper.delete(
                new LambdaQueryWrapper<PersonRelation>()
                        .eq(PersonRelation::getEntityType, EntityType.ITEM.getValue())
                        .in(PersonRelation::getEntityId, ids)
        );

        return I18nHelper.getMessage("entity.curd.delete.success");
    }

    //region

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
                .itemImageInfo(CommonImageUtil.segmentItemImages(item.getType(), item.getImages()))
                .personnel(personSrv.getPersonnel(EntityType.ITEM.getValue(), id))
                .build();
    }

    @Transactional
    public SearchResult<ItemMiniVO> search(SimpleSearchParam param) {
        if (param.keywordEmpty()) new SearchResult<>();

        LambdaQueryWrapper<Item> wrapper = new LambdaQueryWrapper<Item>()
                .or().like(Item::getName, param.getKeyword())
                .or().like(Item::getNameZh, param.getKeyword())
                .or().like(Item::getNameEn, param.getKeyword())
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
                .selectAll(subClass, "t1")
                .leftJoin(STR."\{MyBatisUtil.getTableName(subClass)} t1 on t1.id = t.id")
                .eq(Item::getType, param.getType())
                .like(StringUtils.isNotBlank(param.getName()), Item::getName, param.getName())
                .like(StringUtils.isNotBlank(param.getNameZh()), Item::getNameZh, param.getNameZh())
                .like(StringUtils.isNotBlank(param.getNameEn()), Item::getNameEn, param.getNameEn())
                .like(StringUtils.isNotBlank(param.getEan13()), Item::getEan13, param.getEan13())
                .like(param.getHasBonus() != null, Item::getHasBonus, Boolean.TRUE.equals(param.getHasBonus()) ? 1 : 0)
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