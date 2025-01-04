package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.data.entity.Relation;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Rakbow
 * @since 2025/1/4 17:40
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AutoMapper(target = Relation.class, reverseConvertGenerate = false)
@Data
public class RelatedEntityMiniDTO extends DTO {

    private long roleId;
    private Integer relatedGroup;
    private int relatedEntityType;
    private long relatedEntityId;
    private String remark;

}
