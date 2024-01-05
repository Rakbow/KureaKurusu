package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.rakbow.kureakurusu.data.image.Image;
import com.rakbow.kureakurusu.util.common.DateHelper;
import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2023-05-01 23:36
 */
@Data
@TableName(value = "entry", autoResultMap = true)
public class Entry {

    private Integer id;
    private String name;//名称(原)
    private String nameZh;//名称(中文)
    private String nameEn;//名称(英语)
    private int category;//分类
    private String alias;//别名
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Image> images;//图片列表（JSON字符串）
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
        this.images = new ArrayList<>();
        this.detail = "{}";
        this.description = "";
        this.addedTime = DateHelper.now();
        this.editedTime = DateHelper.now();
        this.remark = "";
        this.status = 1;
    }

}
