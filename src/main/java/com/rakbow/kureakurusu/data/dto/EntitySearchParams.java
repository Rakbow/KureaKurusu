package com.rakbow.kureakurusu.data.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Rakbow
 * @since 2025/1/16 7:38
 */
@Data
public class EntitySearchParams {

    private int page;
    private int size;
    private String sortField;
    private int sortOrder;

    public boolean asc() {
        return this.sortOrder == 1;
    }

    public boolean isSort() {
        return StringUtils.isNotBlank(this.sortField);
    }

}
