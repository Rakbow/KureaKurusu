package com.rakbow.kureakurusu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rakbow.kureakurusu.data.entity.EntityStatistic;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Rakbow
 * @since 2023-02-22 18:58
 */
@Mapper
public interface StatisticMapper extends BaseMapper<EntityStatistic> {
}
