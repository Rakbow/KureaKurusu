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
public class RelationListQueryDTO extends ListQueryDTO {

    private int entityType;
    private long entityId;
    private Integer relatedGroup;

    public RelationListQueryDTO(ListQuery qry) {
        super(qry);
        this.entityType = qry.getVal("entityType");
        this.entityId = ((Integer)qry.getVal("entityId")).longValue();
        this.relatedGroup = qry.getVal("relatedGroup");
    }

}