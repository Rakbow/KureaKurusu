package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.data.dto.EntityMinDTO;
import com.rakbow.kureakurusu.data.dto.EntitySearchParams;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
public class ItemSearchParams extends EntitySearchParams {

    private List<EntityMinDTO> entries;
    private String keyword;
    private Integer type;
    private Integer subType;
    private Integer releaseType;
    private String barcode;
    private String catalogId;
    private String region;
    private Boolean bonus;

    public boolean hasRelatedEntry() {
        return !entries.isEmpty();
    }

    public boolean isAllSearch() {
        return entries.isEmpty()
                && StringUtils.isBlank(keyword)
                && type == null && subType == null
                && releaseType == null
                && StringUtils.isBlank(barcode)
                && StringUtils.isBlank(catalogId)
                && StringUtils.isBlank(region)
                && bonus == null;
    }

}
