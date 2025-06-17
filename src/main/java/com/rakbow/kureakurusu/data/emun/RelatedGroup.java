package com.rakbow.kureakurusu.data.emun;

import com.baomidou.mybatisplus.annotation.EnumValue;
import io.github.linpeilie.annotations.AutoEnumMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rakbow
 * @since 2024/6/20 8:43
 */
@Getter
@AllArgsConstructor
@AutoEnumMapper("value")
public enum RelatedGroup {

    DEFAULT(0, "enum.related_group.default"),
    PRODUCT(1, "enum.related_group.product"),
    PERSON(2, "enum.related_group.person"),
    CHARACTER(3, "enum.related_group.character"),
    CLASSIFICATION(4, "enum.related_group.classification"),
    MATERIAL(5, "enum.related_group.material"),
    EVENT(6, "enum.related_group.event"),
    ITEM(10, "enum.related_group.item");

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
