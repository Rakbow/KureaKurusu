package com.rakbow.kureakurusu.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rakbow.kureakurusu.data.emun.ItemType;
import com.rakbow.kureakurusu.data.entity.item.Item;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2024/1/3 16:12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AutoMappers({
        @AutoMapper(target = Item.class, reverseConvertGenerate = false)
})
public class AlbumUpdateDTO extends ItemUpdateDTO {

    private int discs;
    private int tracks;
    private int runTime;

    public AlbumUpdateDTO() {
        setType(ItemType.ALBUM.getValue());
    }

}