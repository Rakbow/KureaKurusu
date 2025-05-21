package com.rakbow.kureakurusu.data.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Rakbow
 * @since 2025/5/20 18:45
 */
@Data
@NoArgsConstructor
public class EntryListQueryDTO {

    private int SearchType;
    private String name;
    private String nameZh;
    private String nameEn;

    private int page;
    private int size;
    private String sortField;
    private int sortOrder;

    public EntryListQueryDTO(ListQueryDTO dto) {
        size = dto.getRows();
        page = dto.getFirst()/size + 1;
        sortField = dto.getSortField();
        sortOrder = dto.getSortOrder();

        name = dto.getVal("name");
        nameZh = dto.getVal("nameZh");
        nameEn = dto.getVal("nameEn");
    }

    public boolean asc() {
        return this.sortOrder == 1;
    }

    public boolean isSort() {
        return StringUtils.isNotBlank(this.sortField);
    }

}
