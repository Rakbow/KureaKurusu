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
 * @since 2022-10-19 0:26 书籍实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName(value = "book", autoResultMap = true)
public class Book extends MetaEntity {
    private Integer id;//主键编号
    private String title;//标题（原文）
    private String titleEn;//标题（英文）
    private String titleZh;//标题（中文）
    private String isbn10;//国际标准书号（10位）
    private String isbn13;//国际标准书号（13位）
    private int bookType;//书籍类型 0-未分类 1-小说 2-漫画 3-设定集/原画集/公式书 4-其他
    private String franchises;//所属系列
    private String products;//所属产品
    private String region;//地区
    private String lang;//语言
    private String personnel;
    private String authors;//作者（译者，插画，原作者等，json）
    private String companies;//相关企业
    private String serials;//连载载体
    private Date publishDate;//出版日期
    private int price;//出版价格
    private String summary;//简介
    private String specs;
    private String spec;//规格
    private int hasBonus;//是否包含特典
    private String bonus;//特典信息

    public Book() {
        this.id = 0;
        this.title = "";
        this.titleEn = "";
        this.titleZh = "";
        this.isbn10 = "";
        this.isbn13 = "";
        this.bookType = 0;
        this.franchises = "[]";
        this.products = "[]";
        this.region = "";
        this.lang = "";
        this.personnel = "[]";
        this.authors = "[]";
        this.companies = "[]";
        this.serials = "[]";
        this.publishDate = null;
        this.price = 0;
        this.summary = "";
        this.specs = "[]";
        this.spec = "[]";
        this.hasBonus = 0;
        this.bonus = "";
        this.setDescription("");
        this.setImages(new ArrayList<>());
        this.setRemark("");
        this.setAddedTime(DateHelper.now());;
        this.setEditedTime(DateHelper.now());;
        this.setStatus(1);
    }
}
