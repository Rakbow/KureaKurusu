package com.rakbow.kureakurusu.data.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2024/01/07 0:13
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateDetailDTO extends DTO {

    private int entityType;
    private long entityId;
    private String text;

}
