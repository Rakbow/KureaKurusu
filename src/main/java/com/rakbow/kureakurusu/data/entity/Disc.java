package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.rakbow.kureakurusu.data.entity.common.SuperItem;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Rakbow
 * @since 2022-11-27 18:49
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName(value = "disc", autoResultMap = true)
public class Disc extends SuperItem {

//    private Long id;
//    private String catalogNo;
//    private String name;
//    private String nameZh;
//    private String nameEn;
//    private String ean13;
//    private String releaseDate;
//    private double price;//发行价格（含税）
//    private Currency currency;
//    private List<Integer> mediaFormat;
//    private Version version;
//    private String spec;
//    private int hasBonus;
//    private String bonus;
//
//    public Disc () {
//        this.id = 0L;
//        this.catalogNo = "";
//        this.name = "";
//        this.nameZh = "";
//        this.nameEn = "";
//        this.ean13 = "";
//        this.releaseDate = null;
//        this.price = 0;
//        this.currency = Currency.JPY;
//        this.mediaFormat = new ArrayList<>();
//        this.version = Version.REGULAR_EDITION;
//        this.spec = "[]";
//        this.hasBonus = 0;
//        this.bonus = "";
//        this.setDetail("");
//        this.setRemark("");
//        this.setAddedTime(DateHelper.now());
//        this.setEditedTime(DateHelper.now());
//        this.setStatus(true);
//    }

}
