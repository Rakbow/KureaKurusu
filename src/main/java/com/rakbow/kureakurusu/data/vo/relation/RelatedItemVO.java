package com.rakbow.kureakurusu.data.vo.relation;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;

/**
 * @author Rakbow
 * @since 2024/2/28 16:17
 */
@Data
public class RelatedItemVO {

    private int entityType;
    private String entityTypeName;
    private long entityId;
    private String cover;
    private String name;
    private String nameZh;
    private String nameEn;
    private Attribute<Integer> relationType;

}
