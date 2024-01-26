package com.rakbow.kureakurusu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rakbow.kureakurusu.data.entity.Franchise;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系列CRUD
 *
 * @author Rakbow
 * @since 2022-08-20 1:06
 */
@Mapper
public interface FranchiseMapper extends BaseMapper<Franchise> {
}
