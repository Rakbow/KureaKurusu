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

    private Integer type;
    private Long id;
    private String thumb;
    private String name;
    private String subName;

}
