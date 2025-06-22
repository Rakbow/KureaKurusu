package com.rakbow.kureakurusu.data.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Rakbow
 * @since 2025/5/23 19:56
 */
@Data
@NoArgsConstructor
public class ListQueryDTO {

    private int page;
    private int size;
    private String sortField;
    private int sortOrder;

    public ListQueryDTO(ListQuery qry) {
        size = qry.isPage() ? qry.getRows() : -1;
        page = qry.isPage() ? qry.getFirst()/size + 1 : 1;
        sortField = qry.getSortField();
        sortOrder = qry.getSortOrder();
    }

    public boolean asc() {
        return this.sortOrder == 1;
    }

    public boolean isSort() {
        return StringUtils.isNotBlank(this.sortField);
    }

}
