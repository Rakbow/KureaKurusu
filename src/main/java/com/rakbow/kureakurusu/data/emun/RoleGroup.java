package com.rakbow.kureakurusu.data.emun;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rakbow
 * @since 2024/6/20 8:43
 */
@Getter
@AllArgsConstructor
public enum RoleGroup {

    DEFAULT(0),
    RELATED_PERSON(1),
    RELATED_ENTRY(2),
    RELATED_ITEM(3),
    RELATED_CHAR(4);

    @EnumValue
    private final Integer value;

    public static RoleGroup get(Integer value) {
        for (RoleGroup group : RoleGroup.values()) {
            if (group.value == value.intValue())
                return group;
        }
        return DEFAULT;
    }

}
