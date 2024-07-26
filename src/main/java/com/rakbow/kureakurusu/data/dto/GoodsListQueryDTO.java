package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.annotation.QueryColumn;
import com.rakbow.kureakurusu.data.emun.ItemType;
import com.rakbow.kureakurusu.data.entity.QueryColumnType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2024/7/26 17:44
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GoodsListQueryDTO extends ItemListQueryDTO {

    @QueryColumn(type = QueryColumnType.NUMBER)
    private Integer goodsType;

    public GoodsListQueryDTO(ListQueryDTO dto) {
        super(dto);
        super.setType(ItemType.GOODS.getValue());
        this.goodsType = dto.getVal("goodsType");
    }

}
