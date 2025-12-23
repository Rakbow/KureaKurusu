package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.data.entity.Relation;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rakbow
 * @since 2025/1/4 17:40
 */
@NoArgsConstructor
@AutoMapper(target = Relation.class, reverseConvertGenerate = false)
@Data
public class RelatedEntityMiniDTO {

    private long roleId;
    private long relatedRoleId;

    private int relatedEntityType;
    private long relatedEntityId;
    private int relatedEntitySubType;

    private String remark;

}
