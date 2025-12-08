package com.rakbow.kureakurusu.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

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
                && ObjectUtils.isEmpty(this.type)
                && ObjectUtils.isEmpty(this.subType)
                && ObjectUtils.isEmpty(this.releaseType)
                && StringUtils.isEmpty(this.getKeyword())
                && StringUtils.isEmpty(this.region)
                && StringUtils.isEmpty(this.barcode)
                && StringUtils.isEmpty(this.catalogId);
    }

    public boolean hasRelatedEntries() {
        return !this.entries.isEmpty();
    }

}
