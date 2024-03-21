package com.rakbow.kureakurusu.data.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2024/2/28 15:27
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RelationDTO extends DTO {

    private long id;
    private int entityType;
    private long entityId;
    private int relatedEntityType;
    private long relatedEntityId;
    private int relatedType;

}
