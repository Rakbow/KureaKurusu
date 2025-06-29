package com.rakbow.kureakurusu.data.vo.relation;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.Builder;
import lombok.Data;

/**
 * @author Rakbow
 * @since 2024/2/28 16:17
 */
@Data
@Builder
public class RelationVO {

    private long id;//relation id

    private RelationTargetVO target;
    private Attribute<Long> role;

    private String remark;

}
