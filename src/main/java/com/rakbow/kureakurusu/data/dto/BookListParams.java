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
        this.title = super.getStr("title");
        this.isbn10 = super.getStr("isbn10");
        this.isbn13 = super.getStr("isbn13");
        this.region = super.getStr("region");
        this.lang = super.getStr("lang");
        this.bookType = super.getInteger("bookType");
        this.hasBonus = super.getBool("hasBonus");
    }

}