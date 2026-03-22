package com.rakbow.kureakurusu.data.vo.index;

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
public class IndexItemVO {

    private long id;

    private Object target;
    private Object parent;

    private String remark;

}
