package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.toolkit.StringUtil;
import lombok.Data;

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
        return StringUtil.isNotBlank(this.sortField);
    }

}
