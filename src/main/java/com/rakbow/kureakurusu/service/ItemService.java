package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.DeleteJoinWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.rakbow.kureakurusu.dao.ItemMapper;
import com.rakbow.kureakurusu.dao.PersonRelationMapper;
import com.rakbow.kureakurusu.data.ItemTypeRelation;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.dto.AlbumListParams;
import com.rakbow.kureakurusu.data.dto.ItemCreateDTO;
import com.rakbow.kureakurusu.data.dto.ItemUpdateDTO;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.entity.*;
import com.rakbow.kureakurusu.data.entity.common.SuperItem;
import com.rakbow.kureakurusu.data.vo.ItemDetailVO;
import com.rakbow.kureakurusu.data.vo.album.AlbumVOAlpha;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.DateHelper;
import com.rakbow.kureakurusu.util.common.MyBatisUtil;
import com.rakbow.kureakurusu.util.common.RedisUtil;
import com.rakbow.kureakurusu.util.common.VisitUtil;
import com.rakbow.kureakurusu.util.convertMapper.AlbumVOMapper;
import com.rakbow.kureakurusu.util.file.QiniuImageUtil;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
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
        if(relation == null) return null;

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

        long id = mapper.insert(item);
        subItem.setId(id);
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
        if(items.isEmpty()) return;
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

//    public SearchResult<AlbumVOAlpha> list(AlbumListParams param) {
//        MPJLambdaWrapper<Item> wrapper = new MPJLambdaWrapper<Item>()
//                .selectAll(Item.class)
//                .selectAll(ItemAlbum.class)
//                .leftJoin(ItemAlbum.class, ItemAlbum::getId, Item::getEntityId)
//                .like(Item::getName, param.getName())
//                .like(Item::getNameZh, param.getNameZh())
//                .like(Item::getNameEn, param.getNameEn())
//                .like(Item::getEan13, param.getEan13())
//                .like(ItemAlbum::getCatalogNo, param.getCatalogNo())
//                .in(CollectionUtils.isNotEmpty(param.getAlbumFormat()), ItemAlbum::getAlbumFormat, param.getAlbumFormat())
//                .in(CollectionUtils.isNotEmpty(param.getPublishFormat()), ItemAlbum::getPublishFormat, param.getPublishFormat())
//                .in(CollectionUtils.isNotEmpty(param.getMediaFormat()), ItemAlbum::getMediaFormat, param.getMediaFormat())
//                .orderBy(param.isSort(), param.asc(), param.sortField);
//        IPage<Album> pages = mapper.selectJoinPage(new Page<>(param.getPage(), param.getSize()), Album.class, wrapper);
//        List<AlbumVOAlpha> items = albumVOMapper.toVOAlpha(pages.getRecords());
//        return new SearchResult<>(items, pages.getTotal(), pages.getCurrent(), pages.getSize());
//    }

    @SneakyThrows
    public ItemTypeRelation getItemTypeRelation(long id) {
        String redisKey = STR."item_type_related:\{id}";
        if(!redisUtil.hasKey(redisKey)) return null;
        return redisUtil.get(redisKey, ItemTypeRelation.class);
    }
}
