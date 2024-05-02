package com.rakbow.kureakurusu.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rakbow.kureakurusu.data.entity.Item;
import com.rakbow.kureakurusu.data.entity.ItemBook;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import jakarta.validation.constraints.NotBlank;
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

    private String isbn10;
    private int bookType;
    private String region;
    private String lang;
    private String summary;
    private boolean hasBonus;

}
