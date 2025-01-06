package com.rakbow.kureakurusu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rakbow.kureakurusu.data.entity.entry.Product;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Rakbow
 * @since 2022-08-20 1:49
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {
}
