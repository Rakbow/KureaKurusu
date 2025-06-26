package com.rakbow.kureakurusu.data.vo.item;
import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.vo.EntityListVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/5/5 7:50
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ItemListVO extends EntityListVO {

    private int type;
    private Attribute<Integer> subType;
    private List<String> aliases;

    private Attribute<Integer> releaseType;
    private String barcode;
    private String catalogId;
    private String releaseDate;
    private double price;
    private String currency;
    private String region;
    private boolean bonus;

    private double width;
    private double length;
    private double height;
    private double weight;

    private int fileCount;
    private int imageCount;

}