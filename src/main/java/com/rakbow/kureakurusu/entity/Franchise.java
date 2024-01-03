package com.rakbow.kureakurusu.entity;

import com.rakbow.kureakurusu.entity.common.MetaEntity;
import com.rakbow.kureakurusu.util.common.DateHelper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * @author Rakbow
 * @since 2022-08-20 0:50 系列实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Franchise extends MetaEntity {

    private Integer id;//主键
    private String name;//系列名
    private String nameZh;//系列名（中文）
    private String nameEn;//系列名（英语）
    private Date originDate;//发行日期
    private String metaInfo;//meta相关信息

    public Franchise() {
        this.id = 0;
        this.name = "";
        this.nameZh = "";
        this.nameEn = "";
        this.originDate = null;
        this.metaInfo = "{\"isMeta\": 0, \"ids\": []}";
        this.setAddedTime(DateHelper.NOW_TIMESTAMP);
        this.setEditedTime(DateHelper.NOW_TIMESTAMP);
        this.setDescription("");
        this.setRemark("");
        this.setImages("[]");
        this.setStatus(1);
    }

}
