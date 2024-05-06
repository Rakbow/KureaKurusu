package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.annotation.QueryColumn;
import com.rakbow.kureakurusu.annotation.QueryColumnType;
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
    private String isbn10;
    @QueryColumn
    private String region;
    @QueryColumn
    private String lang;
    @QueryColumn(type = QueryColumnType.NUMBER)
    private Integer bookType;
    @QueryColumn(type = QueryColumnType.BOOLEAN)
    private Boolean hasBonus;

    public BookListQueryDTO(ListQueryDTO dto) {
        super(dto);
        super.setType(ItemType.BOOK.getValue());
        this.isbn10 = dto.getVal("isbn10");
        this.region = dto.getVal("region");
        this.lang = dto.getVal("lang");
        this.bookType = dto.getVal("bookType");
        this.hasBonus = dto.getVal("hasBonus");
    }

}