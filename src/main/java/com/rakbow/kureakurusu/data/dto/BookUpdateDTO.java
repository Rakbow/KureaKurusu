package com.rakbow.kureakurusu.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rakbow.kureakurusu.data.emun.ItemType;
import com.rakbow.kureakurusu.data.emun.Language;
import com.rakbow.kureakurusu.data.entity.Item;
import com.rakbow.kureakurusu.data.entity.ItemBook;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2024/3/4 13:44
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AutoMappers({
        @AutoMapper(target = Item.class, reverseConvertGenerate = false),
        @AutoMapper(target = ItemBook.class, reverseConvertGenerate = false)
})
public class BookUpdateDTO extends ItemUpdateDTO {

    private int pages;
    private String size;
    private String lang;
    private String summary;

    public BookUpdateDTO() {
        setType(ItemType.BOOK.getValue());
        lang = Language.JAPANESE.getValue();
        summary = "";

    }

}
