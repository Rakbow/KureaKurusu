package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.annotation.QueryColumn;
import com.rakbow.kureakurusu.data.emun.ItemType;
import com.rakbow.kureakurusu.data.entity.QueryColumnType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2024/7/26 17:46
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FigureListQueryDTO extends ItemListQueryDTO {

    @QueryColumn(type = QueryColumnType.NUMBER)
    private Integer figureType;

    public FigureListQueryDTO(ListQuery dto) {
        super(dto);
        super.setType(ItemType.FIGURE.getValue());
        this.figureType = dto.getVal("figureType");
    }

}
