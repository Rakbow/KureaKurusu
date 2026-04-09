package com.rakbow.kureakurusu.data.vo.index;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;

/**
 * @author Rakbow
 * @since 2026/4/3 1:15
 */
@Data
public class IndexElementItemVO {

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

    private Long entryId;
    private String entryName;
    private String entrySubName;
    private Attribute<Integer> entrySubType;
    private String entryThumb;

    private String createdAt;

}
