package com.rakbow.kureakurusu.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rakbow.kureakurusu.data.entity.Item;
import com.rakbow.kureakurusu.data.entity.ItemAlbum;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/1/3 16:12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AutoMappers({
        @AutoMapper(target = Item.class, reverseConvertGenerate = false),
        @AutoMapper(target = ItemAlbum.class, reverseConvertGenerate = false)
})
public class AlbumUpdateDTO extends ItemUpdateDTO {

    private String catalogNo;
    @NotEmpty(message = "{entity.crud.publish_format.required_field}")
    private List<Integer> publishFormat;
    @NotEmpty(message = "{entity.crud.album_format.required_field}")
    private List<Integer> albumFormat;
    @NotEmpty(message = "{entity.crud.media_format.required_field}")
    private List<Integer> mediaFormat;
    private boolean hasBonus;

}