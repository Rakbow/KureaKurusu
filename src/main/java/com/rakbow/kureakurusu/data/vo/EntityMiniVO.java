package com.rakbow.kureakurusu.data.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Rakbow
 * @since 2024/6/9 23:38
 */
@Data
@AllArgsConstructor
public class EntityMiniVO {

    private String cover;
    private Integer type;// entity type
    private Long id;// entity id
    private String name;// entity name
    private String subName;
    private String info;

}
