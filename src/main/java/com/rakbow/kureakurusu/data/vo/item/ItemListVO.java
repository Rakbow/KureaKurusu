package com.rakbow.kureakurusu.data.vo.item;
import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/5/5 7:50
 */
@Data
public class ItemListVO {

    private long id;
    private int type;
    private String name;
    private List<String> aliases;

    private Attribute<Integer> releaseType;
    private String barcode;
    private String catalogId;
    private String releaseDate;
    private double price;
    private String currency;
    private String region;
    private boolean bonus;
    private String remark;

    private double width;
    private double length;
    private double height;
    private double weight;

    private String addedTime;
    private String editedTime;
    private boolean status;

    private long visitNum;

}