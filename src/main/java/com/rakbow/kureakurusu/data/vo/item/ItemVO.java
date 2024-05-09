package com.rakbow.kureakurusu.data.vo.item;

import lombok.Data;

/**
 * @author Rakbow
 * @since 2024/5/6 12:59
 */
@Data
public class ItemVO {

    private long id;
    private int type;
    private String name;
    private String nameZh;
    private String nameEn;
    private String ean13;
    private String releaseDate;
    private double price;
    private String currency;
    private String remark;

    private String addedTime;
    private String editedTime;

    private String detail;
    private boolean hasBonus;
    private String bonus;
    private boolean status;

}
