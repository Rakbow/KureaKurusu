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

    private String thumb;//thumb cover
    private Integer type;// entity type
    private Long id;// entity id
    private String tableName;// entity type
    private String name;// entity name

}
