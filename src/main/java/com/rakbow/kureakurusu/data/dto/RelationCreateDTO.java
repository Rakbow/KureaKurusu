package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.data.vo.relation.RelationCreateMiniDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/2/28 15:27
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RelationCreateDTO extends DTO {

    private int entityType;
    private int entitySubType;
    private long entityId;

    private int relatedEntityType;
    private int relatedEntitySubType;
    private List<RelationCreateMiniDTO> relatedEntries;

    private long roleId;
    private long relatedRoleId;

}
