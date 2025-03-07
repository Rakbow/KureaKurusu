package com.rakbow.kureakurusu.dao;

import com.github.yulichang.base.MPJBaseMapper;
import com.rakbow.kureakurusu.data.entity.item.Item;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Rakbow
 * @since 2024-04-08 18:01
 */
@Mapper
public interface ItemMapper extends MPJBaseMapper<Item> {
}
