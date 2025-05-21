package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.annotation.QueryColumn;
import com.rakbow.kureakurusu.data.emun.EntrySearchType;
import com.rakbow.kureakurusu.data.entity.QueryColumnType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2025/5/21 5:12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProductListQueryDTO extends EntryListQueryDTO {

    @QueryColumn(type = QueryColumnType.NUMBER)
    private Integer type;
    @QueryColumn(type = QueryColumnType.STRING)
    private String date;

    public ProductListQueryDTO(ListQueryDTO dto) {
        super(dto);
        super.setSearchType(EntrySearchType.PRODUCT.getValue());
        this.type = dto.getVal("type");
        this.date = dto.getVal("date");
    }

}
