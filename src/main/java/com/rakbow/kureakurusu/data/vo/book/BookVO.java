package com.rakbow.kureakurusu.data.vo.book;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;

/**
 * 转换量最大的VO，一般用于详情页面
 *
 * @author Rakbow
 * @since 2023-01-11 10:42
 */
@Data
public class BookVO {

    //基础信息
    private int id;
    private String title;
    private String titleEn;
    private String titleZh;
    private String isbn10;
    private String isbn13;
    private Attribute<Integer> bookType;
    private String publishDate;
    private Attribute<String> region;
    private Attribute<String> lang;
    private double price;
    private String currency;
    private String remark;
    private boolean hasBonus;

    private String addedTime;
    private String editedTime;
    private String detail;
    private String bonus;
    private boolean status;

}
