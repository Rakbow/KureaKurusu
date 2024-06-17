package com.rakbow.kureakurusu.data.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/5/4 9:04
 */
@Data
@NoArgsConstructor
public class ItemListQueryDTO {

    private int type;
    private String name;
    private String aliases;
    private String barcode;
    private Boolean bonus;
    private String region;
    private Integer releaseType;

    private int page;
    private int size;
    private String sortField;
    private int sortOrder;

    public ItemListQueryDTO(ListQueryDTO dto) {
        size = dto.getRows();
        page = dto.getFirst()/size + 1;
        sortField = dto.getSortField();
        sortOrder = dto.getSortOrder();

        name = dto.getVal("name");
        aliases = dto.getVal("aliases");
        barcode = dto.getVal("barcode");
        region = dto.getVal("region");
        bonus = dto.getVal("bonus");
        releaseType = dto.getVal("releaseType");
    }

    public boolean asc() {
        return this.sortOrder == 1;
    }

    public boolean isSort() {
        return StringUtils.isNotBlank(this.sortField);
    }

}