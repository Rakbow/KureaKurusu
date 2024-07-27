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
    private int type;

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

    private int width;// mm
    private int length;// mm
    private int height;// mm
    private int weight;

    private String remark;
    private String detail;

    private String addedTime;
    private String editedTime;

    private boolean status;

}
