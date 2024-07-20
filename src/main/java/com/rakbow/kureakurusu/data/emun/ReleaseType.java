package com.rakbow.kureakurusu.data.emun;

import com.baomidou.mybatisplus.annotation.EnumValue;
import io.github.linpeilie.annotations.AutoEnumMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@AutoEnumMapper("value")
public enum ReleaseType {

    STANDARD(0, "enum.release_type.standard"),
    LIMITED(1, "enum.release_type.limited"),
    EXCLUSIVE(2, "enum.release_type.exclusive"),
    LIMITED_EXCLUSIVE(3, "enum.release_type.limited_exclusive"),
    PRIZE(4, "enum.release_type.prize"),
    INITIAL_PRESS(5, "enum.release_type.initial_press"),
    UNKNOWN(99, "enum.release_type.unknown");

    @EnumValue
    private final Integer value;
    private final String labelKey;

    public static ReleaseType get(int value) {
        for (ReleaseType type : ReleaseType.values()) {
            if(type.value == value)
                return type;
        }
        return UNKNOWN;
    }

}
