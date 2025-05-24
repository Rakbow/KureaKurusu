package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.annotation.QueryColumn;
import com.rakbow.kureakurusu.data.entity.QueryColumnType;
import com.rakbow.kureakurusu.data.emun.ItemType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2024/5/6 12:11
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BookListQueryDTO extends ItemListQueryDTO {

    @QueryColumn
    private String lang;
    @QueryColumn(type = QueryColumnType.NUMBER)
    private Integer bookType;

    public BookListQueryDTO(ListQuery dto) {
        super(dto);
        super.setType(ItemType.BOOK.getValue());
        this.lang = dto.getVal("lang");
        this.bookType = dto.getVal("bookType");
    }

}