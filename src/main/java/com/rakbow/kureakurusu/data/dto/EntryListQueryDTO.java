package com.rakbow.kureakurusu.data.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2025/5/20 18:45
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EntryListQueryDTO extends ListQueryDTO {

    private int type;

    public void init() {
        super.init();
        type = super.getVal("type");
    }

}
