package com.rakbow.kureakurusu.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Rakbow
 * @since 2025/1/16 5:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemSearchQueryDTO extends ListQueryDTO {

    private List<Long> entries;
    private Integer listId;

    private Integer type;
    private Integer subType;
    private Integer releaseType;
    private String region;
    private String barcode;
    private String catalogId;

    public void init() {
        entries = super.getVal("entries");
        listId = super.getVal("listId");

        type = super.getVal("type");
        subType = super.getVal("subType");
        releaseType = super.getVal("releaseType");
        region = super.getVal("region");
        barcode = super.getVal("barcode");
        catalogId = super.getVal("catalogId");
        
        super.setKeyword(super.getVal("keyword"));
    }

    public boolean hasRelatedEntries() {
        return !this.entries.isEmpty();
    }

}
