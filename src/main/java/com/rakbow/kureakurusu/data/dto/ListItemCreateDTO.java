package com.rakbow.kureakurusu.data.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Rakbow
 * @since 2025/7/24 22:20
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ListItemCreateDTO extends DTO {

    private long listId;
    private int type;
    private List<Long> itemIds;

}
