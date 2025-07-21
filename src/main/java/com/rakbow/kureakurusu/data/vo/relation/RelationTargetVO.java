package com.rakbow.kureakurusu.data.vo.relation;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rakbow
 * @since 2025/6/30 2:27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelationTargetVO {

    private int entityType;
    private long entityId;
    private String name;
    private String subName;
    private String thumb;

    private Attribute<Integer> subType;
    private Attribute<Long> role;

}
