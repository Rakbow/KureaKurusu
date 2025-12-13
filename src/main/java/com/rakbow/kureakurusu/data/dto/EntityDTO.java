package com.rakbow.kureakurusu.data.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2025/7/3 2:56
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EntityDTO extends DTO {

    private int entityType;
    private long entityId;

}
