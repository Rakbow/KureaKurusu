package com.rakbow.kureakurusu.data.emun;

import com.baomidou.mybatisplus.annotation.EnumValue;
import io.github.linpeilie.annotations.AutoEnumMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rakbow
 * @since 2024/5/21 16:44
 */
@Getter
@AllArgsConstructor
@AutoEnumMapper("value")
public enum Version {

    REGULAR_EDITION(0, "enum.version.regular_edition"),
    LIMITED_EDITION(1, "enum.version.limited_edition");

    @EnumValue
    private final int value;
    private final String labelKey;

    public static Version get(int value) {
        for (Version version : Version.values()) {
            if(version.value == value)
                return version;
        }
        return REGULAR_EDITION;
    }

}
