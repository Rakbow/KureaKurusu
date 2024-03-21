package com.rakbow.kureakurusu.data.emun;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rakbow
 * @since 2023-10-30 17:51
 */
@Getter
@AllArgsConstructor
public enum Gender {

    UNKNOWN(0, "enum.gender.unknown"),
    MALE(1, "enum.gender.male"),
    FEMALE(2, "enum.gender.female");

    @EnumValue
    private final Integer value;
    private final String labelKey;

    public static Gender get(int value) {
        for (Gender gender : Gender.values()) {
            if(gender.value == value)
                return gender;
        }
        return UNKNOWN;
    }

}
