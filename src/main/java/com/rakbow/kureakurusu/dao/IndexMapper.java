package com.rakbow.kureakurusu.dao;

import com.github.yulichang.base.MPJBaseMapper;
import com.rakbow.kureakurusu.data.dto.IndexItemSearchQueryDTO;
import com.rakbow.kureakurusu.data.entity.Index;
import com.rakbow.kureakurusu.data.vo.item.ItemSimpleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Rakbow
 * @since 2025/7/24 21:55
 */
@Mapper
public interface IndexMapper extends MPJBaseMapper<Index> {

    List<ItemSimpleVO> getItemGroupByEntry(@Param("param") IndexItemSearchQueryDTO param);

    long countItemGroupByEntry(@Param("param") IndexItemSearchQueryDTO param);

}
