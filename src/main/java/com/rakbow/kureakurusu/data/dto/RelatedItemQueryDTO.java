package com.rakbow.kureakurusu.data.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2024/7/27 17:04
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RelatedItemQueryDTO extends QueryDTO {

    private int entityType;
    private long entityId;
    private int page;
    private int size;
    
}
