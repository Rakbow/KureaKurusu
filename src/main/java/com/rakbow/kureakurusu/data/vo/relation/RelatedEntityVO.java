package com.rakbow.kureakurusu.data.vo.relation;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author Rakbow
 * @since 2025/1/1 1:30
 */
@Data
@Builder
@AllArgsConstructor
public class RelatedEntityVO {

    private Integer type;// entity type
    private Long id;// entity id
    private String name;// entity name
    private String subName;
    private String cover;
    private Attribute<Integer> relatedGroup;
    private Attribute<Long> role;
    private Attribute<Long> reverseRole;

}
