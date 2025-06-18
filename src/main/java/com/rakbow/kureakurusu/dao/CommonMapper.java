package com.rakbow.kureakurusu.dao;

import org.apache.ibatis.annotations.Mapper;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Rakbow
 * @since 2022-10-07 8:13
 */
@Mapper
public interface CommonMapper {

    //修改状态(批量)
    void updateEntityStatus(String tableName, List<Long> ids, int status);
    //通用更新描述
    void updateEntityDetail(String tableName, long entityId, String text, Timestamp editedTime);

}
