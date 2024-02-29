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
    private int entityType;
    private long entityId;
    private Attribute<Integer> type;
    private int action;

    public RelationPair() {
        id = 0L;
        entityType = 0;
        entityId = 0L;
        type = new Attribute<>();
        action = DataActionType.NO_ACTION.getValue();
    }

}
