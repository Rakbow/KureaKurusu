package com.rakbow.kureakurusu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.rakbow.kureakurusu.entity.common.MetaEntity;
import com.rakbow.kureakurusu.util.common.DateHelper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author Rakbow
 * @since 2023-01-06 14:43
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName(value = "game", autoResultMap = true)
public class Game extends MetaEntity {

    private Integer id;//主键编号
    private String name;//游戏名（原文）
    private String nameZh;//游戏名（中文）
    private String nameEn;//游戏名（英文）
    private String barcode;//商品条形码
    private String franchises;//所属系列
    private String products;//所属产品
    private Date releaseDate;//发售日期
    private int releaseType;//发售类型
    private int platform;//平台
    private String region;//地区
    private int hasBonus;//是否包含特典
    private String organizations;//相关组织
    private String staffs;//开发制作人员
    private String bonus;//特典信息

    public Game () {
        this.id = 0;
        this.name = "";
        this.nameZh = "";
        this.nameEn = "";
        this.barcode = "";
        this.franchises = "[]";
        this.products = "[]";
        this.releaseDate = null;
        this.releaseType = 0;
        this.platform = 0;
        this.region = "";
        this.hasBonus = 0;
        this.organizations = "[]";
        this.staffs = "[]";
        this.bonus = "";
        this.setImages(new ArrayList<>());
        this.setDescription("");
        this.setRemark("");
        this.setAddedTime(DateHelper.now());;
        this.setEditedTime(DateHelper.now());;
        this.setStatus(1);
    }

}
