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
public enum RelatedGroup {

    DEFAULT(0),
    RELATED_PERSON(1),
    RELATED_ENTRY(2),
    RELATED_ITEM(3),
    RELATED_CHAR(4),
    RELATED_PRODUCT(5),
    MATERIAL(6),
    EVENT(7);

    @EnumValue
    private final Integer value;

    public static RelatedGroup get(Integer value) {
        for (RelatedGroup group : RelatedGroup.values()) {
            if (group.value == value.intValue())
                return group;
        }
        return DEFAULT;
    }

}
