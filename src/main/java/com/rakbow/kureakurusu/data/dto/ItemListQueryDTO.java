package com.rakbow.kureakurusu.data.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Rakbow
 * @since 2024/5/4 9:04
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ItemListQueryDTO extends ListQueryDTO {

    private int type;

    public void init() {
        super.init();
        type = super.getVal("type");
    }

}