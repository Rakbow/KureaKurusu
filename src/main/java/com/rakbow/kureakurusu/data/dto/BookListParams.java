package com.rakbow.kureakurusu.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Rakbow
 * @since 2024/3/22 11:41
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookListParams extends ListQueryParams {

    private String title;
    private String isbn10;
    private String isbn13;
    private String region;
    private String lang;
    private Integer bookType;
    private Boolean hasBonus;

    public BookListParams(ListQueryDTO qry) {
        super(qry);
        this.title = super.getVal("title");
        this.isbn10 = super.getVal("isbn10");
        this.isbn13 = super.getVal("isbn13");
        this.region = super.getVal("region");
        this.lang = super.getVal("lang");
        this.bookType = super.getVal("bookType");
        this.hasBonus = super.getVal("hasBonus");
    }

}