package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.data.entity.Relation;
import io.github.linpeilie.annotations.AutoMapper;

/**
 * @author Rakbow
 * @since 2025/1/4 17:40
 */
@AutoMapper(target = Relation.class, reverseConvertGenerate = false)
public record RelatedEntityMiniDTO(
        long roleId,
        long relatedRoleId,
        int relatedEntityType,
        long relatedEntityId,
        int relatedEntitySubType,
        String remark
) {
}
