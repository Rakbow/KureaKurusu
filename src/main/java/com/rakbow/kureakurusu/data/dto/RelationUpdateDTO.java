package com.rakbow.kureakurusu.data.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2024/6/22 17:49
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RelationUpdateDTO extends DTO {

    private long id;
    private long roleId;
    private long relatedRoleId;
    private String remark;
    private Boolean direction;

}