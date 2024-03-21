package com.rakbow.kureakurusu.data.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rakbow.kureakurusu.data.emun.Currency;
import com.rakbow.kureakurusu.data.entity.common.MetaEntity;
import com.rakbow.kureakurusu.util.common.DateHelper;
import com.rakbow.kureakurusu.util.handler.IntegerListHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2022-07-19 0:55 专辑实体类
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@TableName(value = "album", autoResultMap = true)
public class Album extends MetaEntity {

    private Long id;//表主键
    private String catalogNo;//专辑编号
    private String name;//专辑名称（日语）
    private String nameZh;//专辑名称（中文）
    private String nameEn;//专辑名称（英语）
    private String barcode;//商品条形码
    private String releaseDate;//发行日期
    @TableField(typeHandler = IntegerListHandler.class)
    private List<Integer> publishFormat;//出版形式 在mysql中以数组字符串形式存储
    @TableField(typeHandler = IntegerListHandler.class)
    private List<Integer> albumFormat;//专辑分类 在mysql中以数组字符串形式存储
    @TableField(typeHandler = IntegerListHandler.class)
    private List<Integer> mediaFormat;//媒体类型
    private double price;//发行价格（含税）
    private Currency currency;
    private Boolean hasBonus;//是否包含特典内容 0-无 1-有
    private String bonus;//特典信息

    public Album() {

        this.id = 0L;
        this.catalogNo = "";
        this.name = "";
        this.nameZh = "";
        this.nameEn = "";
        this.barcode = "";
        this.releaseDate = "";
        this.publishFormat = new ArrayList<>();
        this.albumFormat = new ArrayList<>();
        this.mediaFormat = new ArrayList<>();
        this.price = 0;
        this.currency = Currency.JPY;
        this.hasBonus = false;
        this.bonus = "";
        this.setDetail("");
        this.setRemark("");
        this.setImages(new ArrayList<>());
        this.setAddedTime(DateHelper.now());
        this.setEditedTime(DateHelper.now());
        this.setStatus(true);

    }
}
