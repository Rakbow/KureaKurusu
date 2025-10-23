package com.rakbow.kureakurusu.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
 * @since 2025/6/6 19:35
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AutoMappers({
        @AutoMapper(target = Item.class, reverseConvertGenerate = false)
})
public class DiscUpdateDTO extends ItemUpdateDTO {

    private int discs;
    private int episodes;
    private int runTime;
    @NotEmpty(message = "{entity.crud.media_format.required_field}")
    private List<Integer> mediaFormat;

    public DiscUpdateDTO() {
        setType(ItemType.DISC.getValue());
        mediaFormat = new ArrayList<>();
    }

}
