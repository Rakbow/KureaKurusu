package com.rakbow.kureakurusu.data.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Rakbow
 * @since 2024/5/4 9:04
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class ItemListQueryDTO extends ListQueryDTO {

    private int type;

    public ItemListQueryDTO(ListQuery qry) {
        super(qry);
        type = qry.getVal("type");
    }

}