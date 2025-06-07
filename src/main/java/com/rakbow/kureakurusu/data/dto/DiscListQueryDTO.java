package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.annotation.QueryColumn;
import com.rakbow.kureakurusu.data.emun.ItemType;
import com.rakbow.kureakurusu.data.entity.QueryColumnType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Rakbow
 * @since 2025/6/6 18:26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DiscListQueryDTO extends ItemListQueryDTO {

    @QueryColumn(type = QueryColumnType.NUMBER_LIST)
    private List<Integer> mediaFormat;

    public DiscListQueryDTO(ListQuery dto) {
        super(dto);
        super.setType(ItemType.DISC.getValue());
        this.mediaFormat = dto.getArray("mediaFormat");
    }

}
