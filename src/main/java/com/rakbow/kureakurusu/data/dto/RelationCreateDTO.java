package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.data.vo.relation.RelationMiniVO;
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
    private long entityId;
    private int relatedEntityType;
    private int relatedEntryType;
    private List<RelationMiniVO> relatedEntityIds;
    private long roleId;

}
