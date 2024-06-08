package com.rakbow.kureakurusu.data.emun;

import com.baomidou.mybatisplus.annotation.EnumValue;
import io.github.linpeilie.annotations.AutoEnumMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Locale;

/**
 * @author Rakbow
 * @since 2022-12-29 21:00
 */
@Getter
@AllArgsConstructor
@AutoEnumMapper("value")
public enum Region {

    GLOBAL("global", "enum.region.global"),
    JAPAN("jp", "enum.region.japan"),
    CHINA("cn", "enum.region.china"),
    TAIWAN("tw", "enum.region.taiwan"),
    EUROPE("eu", "enum.region.europe"),
    UNITED_STATES("us", "enum.region.united_states");

    @EnumValue
    private final String value;
    private final String labelKey;

    public static Region get(String value) {
        for (Region region : Region.values()) {
            if(region.value.equals(value))
                return region;
        }
        return GLOBAL;
    }

}
