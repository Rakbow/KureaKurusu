package com.rakbow.kureakurusu.data.emun;

import com.baomidou.mybatisplus.annotation.EnumValue;
import io.github.linpeilie.annotations.AutoEnumMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rakbow
 * @since 2024/6/24 19:21
 */
@AllArgsConstructor
@Getter
@AutoEnumMapper("value")
public enum ProductType {

    ANIME(0, "enum.product_type.anime"),
    NOVEL(1, "enum.product_type.novel"),
    COMIC(2, "enum.product_type.comic"),
    GAME(3, "enum.product_type.game"),
    MOVIE(4, "enum.product_type.movie"),
    TV_SERIES(5, "enum.product_type.tv_series"),
    OTHER(99, "enum.product_type.other"),
    MAIN_SERIES(100, "enum.product_type.main_series");

    @EnumValue
    private final Integer value;
    private final String labelKey;

    public static ProductType get(int value) {
        for (ProductType type : ProductType.values()) {
            if(type.value == value)
                return type;
        }
        return OTHER;
    }

}
