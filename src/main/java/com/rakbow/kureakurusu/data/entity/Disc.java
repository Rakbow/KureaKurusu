package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.rakbow.kureakurusu.data.entity.common.MetaEntity;
import com.rakbow.kureakurusu.util.common.DateHelper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author Rakbow
 * @since 2022-11-27 18:49
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName(value = "disc", autoResultMap = true)
public class Disc extends MetaEntity {

    private Integer id;//主键
    private String catalogNo;//商品型番
    private String name;//商品名(原语言)
    private String nameZh;//商品译名(中)
    private String nameEn;//商品译名(英)
    private String barcode;//商品条形码
    private String region;//地区
    private String franchises;//所属系列id
    private String products;//所属作品id
    private Date releaseDate;//发行日期
    private int price;//发行价格
    private String currencyUnit;//货币单位
    private String mediaFormat;//媒体格式 0-未分类 1-DVD 2-Blu-ray
    private int limited;//发售版本是否为限定版 0-否 1-是
    private String spec;//商品规格
    private int hasBonus;//是否包含特典
    private String bonus;//特典信息

    public Disc () {
        this.id = 0;
        this.catalogNo = "";
        this.name = "";
        this.nameZh = "";
        this.nameEn = "";
        this.barcode = "";
        this.region = "";
        this.franchises = "[]";
        this.products = "[]";
        this.releaseDate = null;
        this.price = 0;
        this.currencyUnit = "";
        this.mediaFormat = "[]";
        this.limited = 0;
        this.spec = "[]";
        this.hasBonus = 0;
        this.bonus = "";
        this.setImages(new ArrayList<>());
        this.setDetail("");
        this.setRemark("");
        this.setAddedTime(DateHelper.now());
        this.setEditedTime(DateHelper.now());
        this.setStatus(true);
    }


}
