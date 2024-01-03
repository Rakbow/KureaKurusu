package com.rakbow.kureakurusu.entity.common;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Rakbow
 * @since 2023-05-02 7:48
 */
@Data
public class MetaEntry {

    private int id;
    private Attribute<Integer> category;//分类
    private String name;//原名
    private String nameZh;//中文名
    private String nameEn;//英文名
    private List<String> alias;//别名 json数组
    private String description;//描述
    private String remark;//备注
    private String addedTime;//数据新增时间
    private String editedTime;//数据更新时间

}
