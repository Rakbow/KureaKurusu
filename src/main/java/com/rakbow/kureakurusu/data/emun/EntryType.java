package com.rakbow.kureakurusu.data.emun;

import com.baomidou.mybatisplus.annotation.EnumValue;
import io.github.linpeilie.annotations.AutoEnumMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rakbow
 * @since 2024/12/28 1:38
 */
@Getter
@AllArgsConstructor
@AutoEnumMapper("value")
public enum EntryType {

    PRODUCT(1, "enum.entry.product"),
    PERSON(2, "enum.entry.person"),
    CHARACTER(3, "enum.entry.character"),
    CLASSIFICATION(4, "enum.entry.classification"),
    MATERIAL(5, "enum.entry.material"),
    EVENT(6, "enum.entry.event");

    @EnumValue
    private final Integer value;
    private final String labelKey;

    public static EntryType get(int value) {
        for (EntryType type : EntryType.values()) {
            if(type.value == value)
                return type;
        }
        return PRODUCT;
    }

}
