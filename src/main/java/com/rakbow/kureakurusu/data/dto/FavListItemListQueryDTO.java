package com.rakbow.kureakurusu.data.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2025/7/24 22:44
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FavListItemListQueryDTO extends ListQueryDTO {

    private long listId;
    private int type;

    public void init() {
        listId = ((Integer) super.getVal("listId")).longValue();
        type = super.getVal("type");
    }

}
