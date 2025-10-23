package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.data.emun.ItemType;
import com.rakbow.kureakurusu.data.entity.item.Item;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2024/1/3 14:49
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AutoMappers({
        @AutoMapper(target = Item.class, reverseConvertGenerate = false)
})
public class AlbumCreateDTO extends ItemCreateDTO {

    private int discs;
    private int tracks;
    private int runTime;
    private AlbumDiscCreateDTO disc;
    @NotEmpty(message = "{album.crud.album_format.required_field}")
    private List<Integer> albumFormat;
    @NotEmpty(message = "{entity.crud.media_format.required_field}")
    private List<Integer> mediaFormat;

    public AlbumCreateDTO() {
        setType(ItemType.ALBUM.getValue());
        albumFormat = new ArrayList<>();
        mediaFormat = new ArrayList<>();
    }

}
