package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.data.vo.relation.RelationCreateMiniDTO;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/2/28 15:27
 */
public record RelationCreateDTO(
        int entityType,
        int entitySubType,
        long entityId,
        int relatedEntityType,
        int relatedEntitySubType,
        List<RelationCreateMiniDTO> relatedEntries,
        long roleId,
        long relatedRoleId
) {
}
