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

    DEFAULT(0, "enum.related_group.default"),
    RELATED_PERSON(1, "enum.related_group.related_person"),
    RELATED_ENTRY(2, "enum.related_group.related_entry"),
    RELATED_ITEM(3, "enum.related_group.related_item"),
    RELATED_CHAR(4, "enum.related_group.related_char"),
    RELATED_PRODUCT(5, "enum.related_group.related_product"),
    MATERIAL(6, "enum.related_group.material"),
    EVENT(7, "enum.related_group.event");

    @EnumValue
    private final Integer value;
    private final String labelKey;

    public static RelatedGroup get(int value) {
        for (RelatedGroup group : RelatedGroup.values()) {
            if (group.value == value)
                return group;
        }
        return DEFAULT;
    }

}
