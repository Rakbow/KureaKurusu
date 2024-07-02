package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.data.emun.BookType;
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
 * @since 2024/3/4 14:32
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AutoMappers({
        @AutoMapper(target = Item.class, reverseConvertGenerate = false),
        @AutoMapper(target = ItemBook.class, reverseConvertGenerate = false)
})
public class BookCreateDTO extends ItemCreateDTO {

    private int pages;
    private String size;
    private Integer bookType;
    private String lang;
    private String summary;

    public BookCreateDTO() {
        setType(ItemType.BOOK.getValue());
        bookType = BookType.NOVEL.getValue();
        lang = Language.JAPANESE.getValue();
        summary = "";
    }

}