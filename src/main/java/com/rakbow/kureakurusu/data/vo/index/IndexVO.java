package com.rakbow.kureakurusu.data.vo.index;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;

/**
 * @author Rakbow
 * @since 2025/7/25 14:06
 */
@Data
public class IndexVO {

    private Long id;
    private Attribute<Integer> type;
    private String name;
    private String cover;
    private String createdBy;
    private String createdAt;
    private String updatedAt;
    private String remark;

}
