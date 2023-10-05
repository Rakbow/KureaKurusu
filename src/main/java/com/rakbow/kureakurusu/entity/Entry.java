package com.rakbow.kureakurusu.entity;

import com.rakbow.kureakurusu.util.common.DateHelper;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-05-01 23:36
 * @Description:
 */
@Data
public class Entry {

    private int id;
    private String name;//名称(原)
    private String nameZh;//名称(中文)
    private String nameEn;//名称(英语)
    private int category;//分类
    private String alias;//别名
    private String images;//图片
    private String detail;//额外信息
    private String description;//描述
    private Timestamp addedTime;//收录时间
    private Timestamp editedTime;//编辑时间
    private String remark;//备注
    private int status;//状态

    public Entry() {
        this.id = 0;
        this.name = "";
        this.nameZh = "";
        this.nameEn = "";
        this.category = 0;
        this.alias = "[]";
        this.images = "[]";
        this.detail = "{}";
        this.description = "";
        this.addedTime = DateHelper.NOW_TIMESTAMP;
        this.editedTime = DateHelper.NOW_TIMESTAMP;
        this.remark = "";
        this.status = 1;
    }

}
