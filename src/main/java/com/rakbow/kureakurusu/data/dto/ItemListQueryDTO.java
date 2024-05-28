package com.rakbow.kureakurusu.data.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Rakbow
 * @since 2024/5/4 9:04
 */
@Data
@NoArgsConstructor
public class ItemListQueryDTO {

    private int type;
    private String name;
    private String nameZh;
    private String nameEn;
    private String ean13;
    private Boolean hasBonus;

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
        nameZh = dto.getVal("nameZh");
        nameEn = dto.getVal("nameEn");
        ean13 = dto.getVal("ean13");
        hasBonus = dto.getVal("hasBonus");
    }

    public boolean asc() {
        return this.sortOrder == 1;
    }

    public boolean isSort() {
        return StringUtils.isNotBlank(this.sortField);
    }

}