package com.rakbow.kureakurusu.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

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

    private Integer type;
    private Integer subType;
    private Integer releaseType;

    private String region;
    private String barcode;
    private String catalogId;

    public void init() {
        type = super.getVal("type");
        subType = super.getVal("subType");
        releaseType = super.getVal("releaseType");
        region = super.getVal("region");
        barcode = super.getVal("barcode");
        catalogId = super.getVal("catalogId");
        entries = super.getVal("entries");
        super.setKeyword(super.getVal("keyword"));
    }

    public boolean allSearch() {
        return this.entries.isEmpty()
                && Objects.nonNull(this.type)
                && Objects.nonNull(this.subType)
                && Objects.nonNull(this.releaseType)
                && Objects.nonNull(this.getKeyword())
                && Objects.nonNull(this.region)
                && Objects.nonNull(this.barcode)
                && Objects.nonNull(this.catalogId);
    }

    public boolean hasRelatedEntries() {
        return !this.entries.isEmpty();
    }

}
