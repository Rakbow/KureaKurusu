package com.rakbow.kureakurusu.dao;

import com.github.yulichang.base.MPJBaseMapper;
import com.rakbow.kureakurusu.data.dto.RelationListQueryDTO;
import com.rakbow.kureakurusu.data.entity.Relation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024-02-28 15:18
 */
@Mapper
public interface RelationMapper extends MPJBaseMapper<Relation> {

    List<Relation> list(@Param("param") RelationListQueryDTO param);
    long count(@Param("param") RelationListQueryDTO param);

}
