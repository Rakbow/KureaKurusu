package com.rakbow.kureakurusu.data.vo.favList;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rakbow
 * @since 2025/7/25 2:23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavListItemVO {

    private long id;

    private FavListItemTargetVO target;
    private Object parent;

    private String remark;

}
