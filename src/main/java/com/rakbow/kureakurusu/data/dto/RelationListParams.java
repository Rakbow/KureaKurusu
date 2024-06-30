package com.rakbow.kureakurusu.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Rakbow
 * @since 2024/6/30 0:16
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelationListParams extends ListQueryParams {

    private int entityType;
    private long entityId;
    private int relatedGroup;
    private int direction;// 1: positive -1: negative

    public RelationListParams(ListQueryDTO qry) {
        super(qry);
        this.entityType = super.getVal("entityType");
        this.entityId = ((Integer)super.getVal("entityId")).longValue();
        this.relatedGroup = super.getVal("relatedGroup");
        this.direction = super.getVal("direction");
    }

}