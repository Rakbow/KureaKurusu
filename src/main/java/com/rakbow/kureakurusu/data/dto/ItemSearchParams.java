package com.rakbow.kureakurusu.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Rakbow
 * @since 2025/1/16 5:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemSearchParams extends EntitySearchParams {

    private Integer entityType;
    private Long entityId;
    private String keyword;
    private Integer type;
    private Integer subType;
    private Integer releaseType;
    private String barcode;
    private String catalogId;
    private String region;
    private Boolean bonus;

    public boolean hasRelatedEntry() {
        return entityType != null;
    }

    public boolean isAllSearch() {
        return entityType == null
                && StringUtils.isBlank(keyword)
                && type == null && subType == null
                && releaseType == null
                && StringUtils.isBlank(barcode)
                && StringUtils.isBlank(catalogId)
                && StringUtils.isBlank(region)
                && bonus == null;
    }

}
