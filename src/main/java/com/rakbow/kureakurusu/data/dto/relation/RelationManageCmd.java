package com.rakbow.kureakurusu.data.dto.relation;

import com.rakbow.kureakurusu.data.dto.base.Command;
import com.rakbow.kureakurusu.data.relation.RelationPair;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/2/29 14:44
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RelationManageCmd extends Command {

    private int entityType;
    private long entityId;
    private List<RelationPair> relations;

}
