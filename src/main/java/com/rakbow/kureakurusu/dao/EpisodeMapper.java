package com.rakbow.kureakurusu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rakbow.kureakurusu.data.entity.Episode;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Rakbow
 * @since 2024/01/08 15:18
 */
@Mapper
public interface EpisodeMapper extends BaseMapper<Episode> {
}
