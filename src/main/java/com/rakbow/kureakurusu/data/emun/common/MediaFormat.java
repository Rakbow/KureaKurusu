package com.rakbow.kureakurusu.data.emun.common;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 媒体类型
 *
 * @author Rakbow
 * @since 2022-08-19 23:04
 */
@Getter
@AllArgsConstructor
public enum MediaFormat {

    UNCATEGORIZED(0, "enum.media_format.uncategorized"),
    CD(1, "enum.media_format.cd"),
    DVD(2, "enum.media_format.dvd"),
    BLU_RAY(3, "enum.media_format.blu_ray"),
    DIGITAL(4, "enum.media_format.digital");

    @EnumValue
    private final Integer value;
    private final String labelKey;

    public static MediaFormat get(int value) {
        for (MediaFormat format : MediaFormat.values()) {
            if(format.value == value)
                return format;
        }
        return UNCATEGORIZED;
    }

}
