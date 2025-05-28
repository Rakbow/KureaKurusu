package com.rakbow.kureakurusu.data.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rakbow
 * @since 2025/5/23 15:18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntityMiniVO {

    private String thumb;
    private Integer type;
    private Long id;
    private String tableName;
    private String name;
    private String subName;

}
