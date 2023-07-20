package com.rakbow.kureakurusu.entity;

import com.rakbow.kureakurusu.entity.common.MetaEntity;
import com.rakbow.kureakurusu.util.common.DateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2022-08-20 0:50
 * @Description: 系列实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Franchise extends MetaEntity {

    private int id;//主键
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
        this.setAddedTime(DateUtil.NOW_TIMESTAMP);
        this.setEditedTime(DateUtil.NOW_TIMESTAMP);
        this.setDescription("");
        this.setRemark("");
        this.setImages("[]");
        this.setStatus(1);
    }

}
