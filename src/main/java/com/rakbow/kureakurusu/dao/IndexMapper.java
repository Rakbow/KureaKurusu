package com.rakbow.kureakurusu.dao;

import com.github.yulichang.base.MPJBaseMapper;
import com.rakbow.kureakurusu.data.entity.Index;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Rakbow
 * @since 2025/7/24 21:55
 */
@Mapper
public interface IndexMapper extends MPJBaseMapper<Index> {

    // IPage<EpisodeSearchVO> episodes(Page<EpisodeSearchVO> page, @Param("listId") long listId, @Param("param") ListQueryDTO param);

}
