package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.data.enums.ItemType;
import com.rakbow.kureakurusu.data.entity.item.Item;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
    private int episodes;
    private int runTime;
    private DiscCreateDTO disc;

    public AlbumCreateDTO() {
        setType(ItemType.ALBUM.getValue());
    }

}
