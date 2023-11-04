package com.rakbow.kureakurusu.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rakbow.kureakurusu.entity.common.MetaEntity;
import com.rakbow.kureakurusu.util.common.DateHelper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2022-07-19 0:55
 * @Description: 专辑实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("album")
public class Album extends MetaEntity {

    private Integer id;//表主键
    private String catalogNo;//专辑编号
    private String name;//专辑名称（日语）
    private String nameZh;//专辑名称（中文）
    private String nameEn;//专辑名称（英语）
    private String barcode;//商品条形码
    // @JsonDeserialize(using = DateDeserializer.class)
    private Date releaseDate;//发行日期
    private String publishFormat;//出版形式 在mysql中以数组字符串形式存储
    private String albumFormat;//专辑分类 在mysql中以数组字符串形式存储
    private String mediaFormat;//媒体类型
    private int price;//发行价格（含税）
    private String currencyUnit;
    private String companies;//相关企业
    private int hasBonus;//是否包含特典内容 0-无 1-有
    private String bonus;//特典信息
    private String franchises;//所属系列
    private String products;//所属产品id 在mysql中以数组字符串形式存储
    private String artists;//staff
    private String trackInfo;//曲目列表（JSON字符串）

    public Album() {

        this.id = 0;
        this.catalogNo = "";
        this.name = "";
        this.nameZh = "";
        this.nameEn = "";
        this.barcode = "";
        this.releaseDate = null;
        this.publishFormat = "[]";
        this.albumFormat = "[]";
        this.mediaFormat = "[]";
        this.price = 0;
        this.currencyUnit = "";
        this.companies = "[]";
        this.hasBonus = 0;
        this.bonus = "";
        this.franchises = "[]";
        this.products = "[]";
        this.setDescription("");
        this.setRemark("");
        this.artists = "[]";
        this.setImages("[]");
        this.trackInfo = "{}";
        this.setAddedTime(DateHelper.NOW_TIMESTAMP);
        this.setEditedTime(DateHelper.NOW_TIMESTAMP);
        this.setStatus(1);

    }
}
