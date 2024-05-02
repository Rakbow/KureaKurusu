package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rakbow.kureakurusu.data.dto.BookUpdateDTO;
import com.rakbow.kureakurusu.data.emun.Currency;
import com.rakbow.kureakurusu.data.emun.Language;
import com.rakbow.kureakurusu.data.emun.Region;
import com.rakbow.kureakurusu.data.emun.BookType;
import com.rakbow.kureakurusu.data.entity.common.MetaEntity;
import com.rakbow.kureakurusu.data.entity.common.SuperItem;
import com.rakbow.kureakurusu.util.common.DateHelper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;

/**
 * @author Rakbow
 * @since 2022-10-19 0:26 书籍实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName(value = "book", autoResultMap = true)
public class Book extends SuperItem {

    private Long id;//主键编号
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private String title;//标题（原文）
    private String titleEn;//标题（英文）
    private String titleZh;//标题（中文）
    @TableField(value = "isbn_10", whereStrategy = FieldStrategy.NOT_EMPTY)
    private String isbn10;//国际标准书号（10位）
    @TableField(value = "isbn_13", whereStrategy = FieldStrategy.NOT_EMPTY)
    private String isbn13;//国际标准书号（13位）
    private BookType bookType;//书籍类型

    private String authors;//作者（译者，插画，原作者等，json）

    private Region region;//地区
    private Language lang;//语言
    private Currency currency;//货币

    private String publishDate;//出版日期
    private double price;//出版价格
    private String summary;//简介
    private String spec;//规格
    private Boolean hasBonus;//是否包含特典
    private String bonus;//特典信息

    public Book() {
        this.id = 0L;
        this.title = "";
        this.titleEn = "";
        this.titleZh = "";
        this.isbn10 = "";
        this.isbn13 = "";
        this.bookType = BookType.UNCATEGORIZED;
        this.region = Region.GLOBAL;
        this.lang = Language.JAPANESE;
        this.authors = "[]";
        this.publishDate = "";
        this.price = 0;
        this.summary = "";
        this.spec = "[]";
        this.hasBonus = false;
        this.bonus = "";
        this.setDetail("");
        this.setImages(new ArrayList<>());
        this.setRemark("");
        this.setAddedTime(DateHelper.now());
        this.setEditedTime(DateHelper.now());
        this.setStatus(true);
    }
}
