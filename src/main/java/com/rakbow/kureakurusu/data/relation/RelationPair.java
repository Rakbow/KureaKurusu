package com.rakbow.kureakurusu.data.relation;

import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.emun.system.DataActionType;
import lombok.Data;

/**
 * @author Rakbow
 * @since 2024/2/29 14:41
 */
@Data
public class RelationPair {

    private Long id;
    private long entityId;
    private Attribute<Integer> entityType;
    private Attribute<Integer> relationType;
    private int action;

    public RelationPair() {
        id = 0L;
        entityId = 0L;
        entityType = new Attribute<>();
        relationType = new Attribute<>();
        action = DataActionType.NO_ACTION.getValue();
    }

}
