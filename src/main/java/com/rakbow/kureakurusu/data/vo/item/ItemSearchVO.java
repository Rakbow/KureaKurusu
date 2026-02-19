package com.rakbow.kureakurusu.data.vo.item;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;

/**
 * @author Rakbow
 * @since 2024/5/7 3:01
 */
@Data
public class ItemSearchVO {

    private long id;
    private String name;
    private Attribute<Integer> type;
    private Attribute<Integer> subType;
    private String thumb;
    private String cover;
    private String releaseDate;
    private String barcode;
    private String catalogId;
    private double price;
    private String currency;
    private String region;
    private Boolean completedFlag;

}
