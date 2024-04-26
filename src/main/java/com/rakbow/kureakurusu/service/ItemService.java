package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.DeleteJoinWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.github.yulichang.wrapper.UpdateJoinWrapper;
import com.rakbow.kureakurusu.dao.ItemAlbumMapper;
import com.rakbow.kureakurusu.dao.ItemMapper;
import com.rakbow.kureakurusu.dao.PersonRelationMapper;
import com.rakbow.kureakurusu.data.ItemTypeRelation;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.dto.*;
import com.rakbow.kureakurusu.data.emun.Entity;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.entity.*;
import com.rakbow.kureakurusu.data.entity.common.SuperItem;
import com.rakbow.kureakurusu.data.vo.ItemDetailVO;
import com.rakbow.kureakurusu.data.vo.album.AlbumVOAlpha;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.MyBatisUtil;
import com.rakbow.kureakurusu.util.common.RedisUtil;
import com.rakbow.kureakurusu.util.common.VisitUtil;
import com.rakbow.kureakurusu.util.convertMapper.AlbumVOMapper;
import com.rakbow.kureakurusu.util.file.QiniuImageUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private final ItemAlbumMapper itemAlbumMapper;

    private final PersonRelationMapper relationMapper;

    private final AlbumVOMapper albumVOMapper;

    private final Map<Integer, Class<? extends SubItem>> sourceClassDic = new HashMap<>() {{
        put(Entity.ALBUM.getValue(), ItemAlbum.class);
        put(Entity.BOOK.getValue(), ItemBook.class);
    }};
    private final Map<Integer, Class<? extends SuperItem>> targetClassDic = new HashMap<>() {{
        put(Entity.ALBUM.getValue(), Album.class);
        put(Entity.BOOK.getValue(), Book.class);
    }};

    @Transactional
    @SneakyThrows
    public ItemDetailVO detail(long id) {
        Object item = getById(id);
        if (item == null)
            throw new Exception(I18nHelper.getMessage("item.url.error"));
        return ItemDetailVO.builder()

                .build();
    }

    @Transactional
    @SneakyThrows
    public Object getById(long id) {
        String redisKey = STR."item_type_related:\{id}";
        if(!redisUtil.hasKey(redisKey)) return null;
        ItemTypeRelation relation = redisUtil.get(redisKey, ItemTypeRelation.class);

        Class<? extends SubItem> sourceClass = sourceClassDic.get(relation.getType());
        Class<? extends SuperItem> targetClass = targetClassDic.get(relation.getType());
        MPJLambdaWrapper<Item> wrapper = new MPJLambdaWrapper<Item>()
                .selectAll(Item.class)
                .selectAll(sourceClass, "t1")
                .leftJoin(STR."\{MyBatisUtil.getTableName(sourceClass)} t1 on t1.id = t.id")
                .eq(Item::getId, id);
        return mapper.selectJoinOne(targetClass, wrapper);
    }

    @Transactional
    @SneakyThrows
    public void delete(List<Long> ids, int type) {
        Class<?> subTable = sourceClassDic.get(type);
        //get original data
        List<Item> items = mapper.selectBatchIds(ids);
        for (Item item : items) {
            //delete all image
            qiniuImageUtil.deleteAllImage(item.getImages());
            //delete visit record
            visitUtil.deleteVisit(EntityType.ITEM.getValue(), item.getId());
        }
        //todo 还未测试过
        //delete
        DeleteJoinWrapper<Item> wrapper = JoinWrappers
                .delete(Item.class)
                .delete(subTable)
                .leftJoin(STR."\{MyBatisUtil.getTableName(subTable)} t1 on t1.id = t.id")
                .in(Item::getId, ids);
        mapper.deleteJoin(wrapper);
        //delete person relation
        relationMapper.delete(
                new LambdaQueryWrapper<PersonRelation>()
                        .eq(PersonRelation::getEntityType, EntityType.ITEM.getValue())
                        .in(PersonRelation::getEntityId, ids)
        );
    }

    @Transactional
    @SneakyThrows
    public void update(ItemUpdateDTO dto) {
        mapper.updateById(new Item(dto));
        if(dto instanceof AlbumItemUpdateDTO) {
            itemAlbumMapper.updateById(((AlbumItemUpdateDTO) dto).toItemAlbum());
        }
    }

    public SearchResult<AlbumVOAlpha> list(AlbumListParams param) {
        MPJLambdaWrapper<Item> wrapper = new MPJLambdaWrapper<Item>()
                .selectAll(Item.class)
                .selectAll(ItemAlbum.class)
                .leftJoin(ItemAlbum.class, ItemAlbum::getId, Item::getEntityId)
                .like(Item::getName, param.getName())
                .like(Item::getNameZh, param.getNameZh())
                .like(Item::getNameEn, param.getNameEn())
                .like(Item::getEan13, param.getEan13())
                .like(ItemAlbum::getCatalogNo, param.getCatalogNo())
                .in(CollectionUtils.isNotEmpty(param.getAlbumFormat()), ItemAlbum::getAlbumFormat, param.getAlbumFormat())
                .in(CollectionUtils.isNotEmpty(param.getPublishFormat()), ItemAlbum::getPublishFormat, param.getPublishFormat())
                .in(CollectionUtils.isNotEmpty(param.getMediaFormat()), ItemAlbum::getMediaFormat, param.getMediaFormat())
                .orderBy(param.isSort(), param.asc(), param.sortField);
        IPage<Album> pages = mapper.selectJoinPage(new Page<>(param.getPage(), param.getSize()), Album.class, wrapper);
        List<AlbumVOAlpha> items = albumVOMapper.toVOAlpha(pages.getRecords());
        return new SearchResult<>(items, pages.getTotal(), pages.getCurrent(), pages.getSize());
    }
}
