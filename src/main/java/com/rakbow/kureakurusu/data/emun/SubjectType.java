package com.rakbow.kureakurusu.data.emun;

import com.baomidou.mybatisplus.annotation.EnumValue;
import io.github.linpeilie.annotations.AutoEnumMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rakbow
 * @since 2024/6/18 18:05
 */
@Getter
@AllArgsConstructor
@AutoEnumMapper("value")
public enum SubjectType {

    CLASSIFICATION(1, "enum.subject_type.classification"),
    MATERIAL(2, "enum.subject_type.material"),
    EVENT(3, "enum.subject_type.event");

    @EnumValue
    private final Integer value;
    private final String labelKey;

    public static SubjectType get(int value) {
        for (SubjectType type : SubjectType.values()) {
            if(type.value == value)
                return type;
        }
        return CLASSIFICATION;
    }

}
