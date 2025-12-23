package com.rakbow.kureakurusu.data.dto;

/**
 * @author Rakbow
 * @since 2024/6/22 17:49
 */
public record RelationUpdateDTO(
        long id,
        long roleId,
        long relatedRoleId,
        String remark,
        Boolean direction
) {
}