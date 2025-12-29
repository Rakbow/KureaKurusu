package com.rakbow.kureakurusu.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.base.MPJBaseMapper;
import com.rakbow.kureakurusu.data.dto.ListQueryDTO;
import com.rakbow.kureakurusu.data.entity.FavList;
import com.rakbow.kureakurusu.data.vo.temp.EpisodeSearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Rakbow
 * @since 2025/7/24 21:55
 */
@Mapper
public interface FavListMapper extends MPJBaseMapper<FavList> {

    // IPage<EpisodeSearchVO> episodes(Page<EpisodeSearchVO> page, @Param("listId") long listId, @Param("param") ListQueryDTO param);

}
