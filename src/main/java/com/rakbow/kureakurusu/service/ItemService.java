package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.UpdateJoinWrapper;
import com.rakbow.kureakurusu.dao.ItemMapper;
import com.rakbow.kureakurusu.data.dto.AlbumUpdateDTO;
import com.rakbow.kureakurusu.data.emun.Entity;
import com.rakbow.kureakurusu.data.entity.Album;
import com.rakbow.kureakurusu.data.entity.Item;
import com.rakbow.kureakurusu.data.entity.ItemAlbum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    private final List<Class<?>> classList = new ArrayList<>();

    public void updateJoinTest(AlbumUpdateDTO dto) {
        Item item = new Item(dto.getName());
        item.setId(dto.getId());
        item.setNameZh(dto.getNameZh());
        item.setNameEn(dto.getNameEn());
        item.setRemark(dto.getRemark());

        ItemAlbum album = new ItemAlbum(dto);
        UpdateJoinWrapper<Item> update = JoinWrappers.update(Item.class)
                //设置两个副表的 set 语句
                .setUpdateEntity(album)
                //address和area 两张表空字段和非空字段一起更新 可以改成如下setUpdateEntityAndNull
                //.setUpdateEntityAndNull(address, area)
                .leftJoin(ItemAlbum.class, ItemAlbum::getId, Item::getEntityId)
                .eq(Item::getEntityId, album.getId())
                .eq(Item::getType, Entity.ALBUM);
        mapper.updateJoin(item, update);
    }


}
