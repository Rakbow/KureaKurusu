package com.rakbow.kureakurusu.data;

import lombok.Data;

/**
 * @author Rakbow
 * @since 2024/4/12 2:15
 */
@Data
public class ItemTypeRelation {

    private long id;
    private int type;

    public ItemTypeRelation() {
        id = 0L;
        type = 0;
    }

}
