package com.rakbow.kureakurusu.dao;

import com.rakbow.kureakurusu.data.image.Image;
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
    void updateEntryStatus(String tableName, List<Long> ids, int status);
    //通用更新描述
    void updateEntryDetail(String tableName, long entityId, String text, Timestamp editedTime);
    //通用更新特典信息
    void updateEntryBonus(String tableName, long entityId, String bonus, Timestamp editedTime);

    String getEntryImages(String tableName, long entityId);
    //更新图片
    void updateEntryImages(String tableName, long entityId, List<Image> images, Timestamp editedTime);

}
