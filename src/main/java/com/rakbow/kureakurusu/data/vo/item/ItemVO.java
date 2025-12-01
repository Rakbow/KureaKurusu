package com.rakbow.kureakurusu.data.vo.item;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/5/6 12:59
 */
@Data
public class ItemVO {

    private long id;
    private Attribute<Integer> type;
    private Attribute<Integer> subType;

    private String name;
    private String nameEn;
    private List<String> aliases;

    private Attribute<Integer> releaseType;
    private String barcode;
    private String catalogId;
    private String releaseDate;
    private double price;
    private String currency;
    private String region;
    private boolean bonus;

    private double width;// mm
    private double length;// mm
    private double height;// mm
    private double weight;

    private String remark;
    private String detail;

    private boolean status;

    private int discs;
    private int runTime;

    private int pages;

    private int episodes;

    private String scale;
    private List<String> versions;

    private String spec;

}
