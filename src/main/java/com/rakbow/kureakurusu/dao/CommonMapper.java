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
    void updateItemStatus(String tableName, List<Long> ids, int status);
    //通用更新描述
    void updateItemDetail(String tableName, long entityId, String text, Timestamp editedTime);
    //通用更新特典信息
    void updateItemBonus(String tableName, long entityId, String bonus, Timestamp editedTime);
    //通用更新规格信息
    void updateItemSpecs(String tableName, long entityId, String specs, Timestamp editedTime);
    String getItemImages(String tableName, long entityId);
    //更新图片
    void updateItemImages(String tableName, long entityId, List<Image> images, Timestamp editedTime);
    //获取数据数
    int getItemAmount(String tableName);

}
