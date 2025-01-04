package com.rakbow.kureakurusu.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Rakbow
 * @since 2024/12/30 20:11
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelatedEntityQueryDTO extends QueryDTO {

    private int relatedGroup;
    private int entityType;
    private long entityId;
    private int first;
    private int row;

}
