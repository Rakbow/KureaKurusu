package com.rakbow.kureakurusu.data.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2024/5/4 9:04
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ItemListQueryDTO extends ListQueryDTO {

    private int type;

}