package com.rakbow.kureakurusu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rakbow.kureakurusu.data.entity.Disc;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Rakbow
 * @since 2022-11-27 19:43
 */
@Mapper
public interface DiscMapper extends BaseMapper<Disc> {
}