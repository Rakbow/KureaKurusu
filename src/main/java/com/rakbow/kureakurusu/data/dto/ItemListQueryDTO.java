package com.rakbow.kureakurusu.data.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Rakbow
 * @since 2024/5/4 9:04
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class ItemListQueryDTO extends ListQueryDTO {

    private int type;
    private String name;
    private String aliases;
    private String barcode;
    private String catalogId;
    private Boolean bonus;
    private String region;
    private Integer releaseType;

    public ItemListQueryDTO(ListQuery qry) {
        super(qry);

        name = qry.getVal("name");
        aliases = qry.getVal("aliases");
        barcode = qry.getVal("barcode");
        catalogId = qry.getVal("catalogId");
        region = qry.getVal("region");
        bonus = qry.getVal("bonus");
        releaseType = qry.getVal("releaseType");
    }

}