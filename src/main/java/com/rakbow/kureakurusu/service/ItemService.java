package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.rakbow.kureakurusu.dao.ItemMapper;
import com.rakbow.kureakurusu.dao.PersonRelationMapper;
import com.rakbow.kureakurusu.data.ItemTypeRelation;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.dto.ItemCreateDTO;
import com.rakbow.kureakurusu.data.dto.ItemListQueryDTO;
import com.rakbow.kureakurusu.data.dto.ItemUpdateDTO;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.entity.Item;
import com.rakbow.kureakurusu.data.entity.PersonRelation;
import com.rakbow.kureakurusu.data.entity.SubItem;
import com.rakbow.kureakurusu.data.entity.common.SuperItem;
import com.rakbow.kureakurusu.data.vo.ItemDetailVO;
import com.rakbow.kureakurusu.data.vo.test.ItemListVO;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.*;
import com.rakbow.kureakurusu.util.file.QiniuImageUtil;
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

    private final RedisUtil redisUtil;
    private final QiniuImageUtil qiniuImageUtil;
    private final VisitUtil visitUtil;

    private final ItemMapper mapper;

    private final PersonRelationMapper relationMapper;

    private final Converter converter;

    @Transactional
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public <T extends SuperItem> T getById(long id) {
        ItemTypeRelation relation = getItemTypeRelation(id);
        if (relation == null) return null;

        Class<? extends SubItem> sourceClass = MyBatisUtil.getSubItem(relation.getType());
        Class<? extends SuperItem> targetClass = MyBatisUtil.getSuperItem(relation.getType());
        MPJLambdaWrapper<Item> wrapper = new MPJLambdaWrapper<Item>()
                .selectAll(Item.class)
                .selectAll(sourceClass, "t1")
                .leftJoin(STR."\{MyBatisUtil.getTableName(sourceClass)} t1 on t1.id = t.id")
                .eq(Item::getId, id);
        return (T) mapper.selectJoinOne(targetClass, wrapper);
    }

    @Transactional
    @SneakyThrows
    public void insert(ItemCreateDTO dto) {
        Class<? extends SubItem> subClass = MyBatisUtil.getSubItem(dto.getType());
        BaseMapper<SubItem> subMapper = MyBatisUtil.getMapper(subClass);

        Item item = converter.convert(dto, Item.class);
        SubItem subItem = converter.convert(dto, subClass);

        mapper.insert(item);
        subItem.setId(item.getId());
        subMapper.insert(subItem);
    }

    @Transactional
    @SneakyThrows
    public void update(ItemUpdateDTO dto) {

        Class<? extends SubItem> subClass = MyBatisUtil.getSubItem(dto.getType());
        BaseMapper<SubItem> subMapper = MyBatisUtil.getMapper(subClass);

        Item item = converter.convert(dto, Item.class);
        item.setEditedTime(DateHelper.now());
        SubItem subItem = converter.convert(dto, subClass);

        mapper.updateById(item);
        subMapper.updateById(subItem);
    }

    @Transactional
    @SneakyThrows
    public void delete(List<Long> ids) {
        //get original data
        List<Item> items = mapper.selectBatchIds(ids);
        if (items.isEmpty()) return;
        for (Item item : items) {
            //delete all image
            qiniuImageUtil.deleteAllImage(item.getImages());
            //delete visit record
            visitUtil.deleteVisit(EntityType.ITEM.getValue(), item.getId());
        }
        int type = items.getFirst().getType().getValue();
        Class<? extends SubItem> subClass = MyBatisUtil.getSubItem(type);
        BaseMapper<SubItem> subMapper = MyBatisUtil.getMapper(subClass);

        mapper.delete(new LambdaQueryWrapper<Item>().in(Item::getId, ids));
        subMapper.delete(new LambdaQueryWrapper<SubItem>().in(SubItem::getId, ids));

        relationMapper.delete(
                new LambdaQueryWrapper<PersonRelation>()
                        .eq(PersonRelation::getEntityType, EntityType.ITEM.getValue())
                        .in(PersonRelation::getEntityId, ids)
        );
    }

    @Transactional
    @SneakyThrows
    public ItemDetailVO detail(long id) {
        SuperItem item = getById(id);
        if (item == null)
            throw new Exception(I18nHelper.getMessage("item.url.error"));
        return ItemDetailVO.builder().build();
    }

    @Transactional
    @SneakyThrows
    public SearchResult<? extends ItemListVO> list(ItemListQueryDTO dto) {

        Class<? extends SuperItem> superClass = MyBatisUtil.getSuperItem(dto.getType());
        Class<? extends SubItem> subClass = MyBatisUtil.getSubItem(dto.getType());
        Class<? extends ItemListVO> itemListVOClass = MyBatisUtil.getItemListVO(dto.getType());

        MPJLambdaWrapper<Item> wrapper = new MPJLambdaWrapper<Item>()
                .selectAll(Item.class)
                .selectAll(subClass, "t1")
                .leftJoin(STR."\{MyBatisUtil.getTableName(subClass)} t1 on t1.id = t.id")
                .eq(Item::getType, dto.getType())
                .like(StringUtils.isNotBlank(dto.getName()), Item::getName, dto.getName())
                .like(StringUtils.isNotBlank(dto.getNameZh()), Item::getNameZh, dto.getNameZh())
                .like(StringUtils.isNotBlank(dto.getNameEn()), Item::getNameEn, dto.getNameEn())
                .like(StringUtils.isNotBlank(dto.getEan13()), Item::getEan13, dto.getEan13())
                .orderBy(dto.isSort(), dto.asc(), CommonUtil.camelToUnderline(dto.getSortField()));
        //private query column to sql
        MyBatisUtil.itemListQueryWrapper(dto, wrapper);

        IPage<? extends SuperItem> page = mapper.selectJoinPage(new Page<>(dto.getPage(), dto.getSize()), superClass, wrapper);

        List<? extends ItemListVO> items = converter.convert(page.getRecords(), itemListVOClass);

        return new SearchResult<>(items, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @SneakyThrows
    public ItemTypeRelation getItemTypeRelation(long id) {
        String redisKey = STR."item_type_related:\{id}";
        if (!redisUtil.hasKey(redisKey)) return null;
        return redisUtil.get(redisKey, ItemTypeRelation.class);
    }
}
