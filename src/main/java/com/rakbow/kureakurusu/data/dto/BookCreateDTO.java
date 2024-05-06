package com.rakbow.kureakurusu.data.dto;

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

    private String isbn10;
    private Integer bookType;
    private String region;
    private String lang;
    private boolean hasBonus;
    private String summary;

}