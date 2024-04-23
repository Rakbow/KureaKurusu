package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.github.yulichang.wrapper.UpdateJoinWrapper;
import com.rakbow.kureakurusu.dao.ItemMapper;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.dto.AlbumListParams;
import com.rakbow.kureakurusu.data.dto.AlbumUpdateDTO;
import com.rakbow.kureakurusu.data.dto.UpdateStatusCmd;
import com.rakbow.kureakurusu.data.emun.Entity;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.entity.*;
import com.rakbow.kureakurusu.data.vo.album.AlbumVOAlpha;
import com.rakbow.kureakurusu.util.common.JsonUtil;
import com.rakbow.kureakurusu.util.convertMapper.AlbumVOMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.configurationprocessor.metadata.ItemMetadata;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2024/4/11 4:07
 */
@Service
@RequiredArgsConstructor
public class ItemService extends ServiceImpl<ItemMapper, Item> {

    private final ItemMapper mapper;
    private final AlbumVOMapper VOMapper;

    private final List<Class<?>> classList = new ArrayList<>();

    public Object getOne(long id, Entity e) {
        MPJLambdaWrapper<Item> wrapper = new MPJLambdaWrapper<Item>()
                .selectAll(Item.class).eq(Item::getId, id);
        if(e == Entity.ALBUM) {
            wrapper.selectAll(ItemAlbum.class)
                    .leftJoin(ItemAlbum.class, ItemAlbum::getId, Item::getEntityId);
            return mapper.selectJoinOne(Album.class, wrapper);
        } else if (e == Entity.BOOK) {
            wrapper.selectAll(ItemBook.class)
                    .leftJoin(ItemBook.class, ItemBook::getId, Item::getEntityId);
            return mapper.selectJoinOne(Book.class, wrapper);
        }
        return null;
    }

    public void update(AlbumUpdateDTO dto) {

        Item item = new Item(dto.getName());
        item.setId(dto.getId());
        item.setNameZh(dto.getNameZh());
        item.setNameEn(dto.getNameEn());
        item.setRemark(dto.getRemark());

        ItemAlbum album = new ItemAlbum(dto);
        UpdateJoinWrapper<Item> update = JoinWrappers
                .update(Item.class)
                //设置副表的set语句
                .setUpdateEntity(album)
                .set(ItemAlbum::getAlbumFormat, JsonUtil.toJson(dto.getAlbumFormat()))
                .set(ItemAlbum::getPublishFormat, JsonUtil.toJson(dto.getPublishFormat()))
                .set(ItemAlbum::getMediaFormat, JsonUtil.toJson(dto.getMediaFormat()))
                .leftJoin(ItemAlbum.class, ItemAlbum::getId, Item::getEntityId)
                .eq(Item::getEntityId, album.getId())
                .eq(Item::getType, Entity.ALBUM);
        mapper.updateJoin(item, update);
    }

    public SearchResult<AlbumVOAlpha> joinPageTest(AlbumListParams param) {
        MPJLambdaWrapper<Item> wrapper = new MPJLambdaWrapper<Item>()
                .selectAll(Item.class)
                .selectAll(ItemAlbum.class)
                .leftJoin(ItemAlbum.class, ItemAlbum::getId, Item::getEntityId)
                .like(Item::getName, param.getName())
                .like(Item::getNameZh, param.getNameZh())
                .like(Item::getNameEn, param.getNameEn())
                .like(ItemAlbum::getCatalogNo, param.getCatalogNo())
                .like(ItemAlbum::getBarcode, param.getBarcode())
                .in(CollectionUtils.isNotEmpty(param.getAlbumFormat()), ItemAlbum::getAlbumFormat, param.getAlbumFormat())
                .in(CollectionUtils.isNotEmpty(param.getPublishFormat()), ItemAlbum::getPublishFormat, param.getPublishFormat())
                .in(CollectionUtils.isNotEmpty(param.getMediaFormat()), ItemAlbum::getMediaFormat, param.getMediaFormat())
                .orderBy(param.isSort(), param.asc(), param.sortField);
        IPage<Album> pages = mapper.selectJoinPage(new Page<>(param.getPage(), param.getSize()), Album.class, wrapper);
        List<AlbumVOAlpha> items = VOMapper.toVOAlpha(pages.getRecords());
        return new SearchResult<>(items, pages.getTotal(), pages.getCurrent(), pages.getSize());
    }


    /**
     * 批量更新数据库实体激活状态
     *
     * @author rakbow
     */
    @Transactional
    public void updateItemStatus(UpdateStatusCmd cmd) {
        mapper.update(
                new LambdaUpdateWrapper<Item>()
                        .set(Item::getStatus, cmd.status())
                        .in(Item::getId, cmd.getIds())
        );
    }

}
