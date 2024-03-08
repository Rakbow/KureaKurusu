package com.rakbow.kureakurusu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rakbow.kureakurusu.data.entity.Game;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Rakbow
 * @since 2023-01-06 14:43
 */
@Mapper
public interface GameMapper extends BaseMapper<Game> {
}
