package com.rakbow.kureakurusu.data.dto.book;

import com.rakbow.kureakurusu.data.dto.base.DTO;
import com.rakbow.kureakurusu.data.emun.common.Currency;
import com.rakbow.kureakurusu.data.emun.common.Language;
import com.rakbow.kureakurusu.data.emun.common.Region;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/3/4 14:32
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BookAddDTO extends DTO {

    private long id;
    @NotBlank(message = "{entity.crud.name.required_field}")
    private String title;
    private String titleZh;
    private String titleEn;
    private String isbn10;
    private String isbn13;
    private String publishDate;
    private int bookType;
    private String region;
    private String lang;
    private double price;
    private String currency;
    private boolean hasBonus;
    private String summary;

}
