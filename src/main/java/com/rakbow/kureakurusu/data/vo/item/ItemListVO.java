package com.rakbow.kureakurusu.data.vo.item;
import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;

/**
 * @author Rakbow
 * @since 2024/5/5 7:50
 */
@Data
public class ItemListVO {

    private long id;
    private int type;
    private String name;

    private Attribute<Integer> releaseType;
    private String barcode;
    private String catalogId;
    private String releaseDate;
    private double price;
    private String currency;
    private String region;
    private boolean bonus;
    private String remark;

    private int width;
    private int length;
    private int height;
    private int weight;

    private String addedTime;
    private String editedTime;
    private boolean status;

    private long visitNum;

}