package com.rakbow.kureakurusu.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Rakbow
 * @since 2024/2/28 16:24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelationQry extends QueryDTO {

    private int relatedGroup;
    private int entityType;
    private long entityId;
    private int direction;// 1-positive -1-negative
    private SearchQry param;

}
