package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.data.emun.ItemType;
import com.rakbow.kureakurusu.data.entity.item.Item;
import com.rakbow.kureakurusu.data.entity.item.ItemDisc;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2025/6/6 18:21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AutoMappers({
        @AutoMapper(target = Item.class, reverseConvertGenerate = false),
        @AutoMapper(target = ItemDisc.class, reverseConvertGenerate = false)
})
public class DiscCreateDTO extends ItemCreateDTO {

    private int discs;
    private int episodes;
    private int runTime;
    @NotEmpty(message = "{entity.crud.media_format.required_field}")
    private List<Integer> mediaFormat;

    public DiscCreateDTO() {
        setType(ItemType.DISC.getValue());
        mediaFormat = new ArrayList<>();
    }

}
