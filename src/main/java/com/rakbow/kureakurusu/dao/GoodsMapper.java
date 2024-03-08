package com.rakbow.kureakurusu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rakbow.kureakurusu.data.entity.Goods;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Rakbow
 * @since 2023-01-04 14:12
 */

@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {
}