package com.rakbow.kureakurusu.entity;

import com.rakbow.kureakurusu.entity.common.MetaEntity;
import com.rakbow.kureakurusu.util.common.DateHelper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * @author Rakbow
 * @since 2023-01-04 10:26 周边实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Merch extends MetaEntity {

    private Integer id;//主键编号
    private String name;//商品名（原文）
    private String nameZh;//商品名（中文）
    private String nameEn;//商品名（英文）
    private String barcode;//商品条形码
    private String franchises;//所属系列
    private String products;//所属产品
    private int category;//商品分类
    private String region;//地区
    private Date releaseDate;//发售日
    private int price;//价格
    private String currencyUnit;//价格单位
    private int notForSale;//是否非卖品
    private String spec;//规格

    public Merch() {
        this.id = 0;
        this.name = "";
        this.nameEn = "";
        this.nameZh = "";
        this.barcode = "";
        this.franchises = "[]";
        this.products = "[]";
        this.category = 0;
        this.releaseDate = null;
        this.price = 0;
        this.currencyUnit = "";
        this.region = "";
        this.notForSale = 0;
        this.spec = "[]";
        this.setDescription("");
        this.setImages("[]");
        this.setRemark("");
        this.setAddedTime(DateHelper.now());;
        this.setEditedTime(DateHelper.now());;
        this.setStatus(1);
    }

}
