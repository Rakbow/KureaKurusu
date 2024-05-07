package com.rakbow.kureakurusu.data.vo.relation;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;

/**
 * @author Rakbow
 * @since 2024/2/28 16:17
 */
@Data
public class RelatedItemVO {

    private long id;
    private String cover;
    private String name;
    private String nameZh;
    private String nameEn;

    private String label;//entity type label

    private Attribute<Integer> entityType;
    private long entityId;

    private Attribute<Integer> relationType;

}
