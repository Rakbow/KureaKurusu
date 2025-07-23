package com.rakbow.kureakurusu.data.emun;

import com.baomidou.mybatisplus.annotation.EnumValue;
import io.github.linpeilie.annotations.AutoEnumMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rakbow
 * @since 2022-11-18 23:01
 */
@Getter
@AllArgsConstructor
@AutoEnumMapper("value")
public enum ImageType {

    DEFAULT(0, "enum.default"),
    THUMB(1, "enum.image_type.thumb"),
    MAIN(2, "enum.image_type.main"),
    OTHER(99, "enum.image_type.other");

    @EnumValue
    private final Integer value;
    private final String labelKey;

    public static ImageType get(int value) {
        for (ImageType type : ImageType.values()) {
            if (type.value == value)
                return type;
        }
        return DEFAULT;
    }

}
