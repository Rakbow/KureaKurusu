package com.rakbow.kureakurusu.data.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2025/7/24 22:28
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FavListQueryDTO extends ListQueryDTO {

    private Integer type;

    public void init() {
        super.init();
        type = super.getVal("type");
    }

}
