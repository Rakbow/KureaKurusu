package com.rakbow.kureakurusu.data.vo.index;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rakbow
 * @since 2025/7/24 23:35
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndexItemTargetVO {

    private int entityType;
    private long entityId;
    private String name;
    private String subInfo;
    private String thumb;

    private Attribute<Integer> subType;

}
