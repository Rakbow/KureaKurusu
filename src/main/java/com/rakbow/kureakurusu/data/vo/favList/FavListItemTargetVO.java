package com.rakbow.kureakurusu.data.vo.favList;

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
public class FavListItemTargetVO {

    private int entityType;
    private long entityId;
    private String name;
    private String thumb;

    private Attribute<Integer> subType;

}
