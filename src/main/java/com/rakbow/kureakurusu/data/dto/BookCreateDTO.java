package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.data.enums.ItemType;
import com.rakbow.kureakurusu.data.entity.item.Item;
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
        @AutoMapper(target = Item.class, reverseConvertGenerate = false)
})
public class BookCreateDTO extends ItemCreateDTO {

    private int pages;
    private String size;

    public BookCreateDTO() {
        setType(ItemType.BOOK.getValue());
    }

}