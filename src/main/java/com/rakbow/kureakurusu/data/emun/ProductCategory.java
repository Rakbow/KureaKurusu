package com.rakbow.kureakurusu.data.emun;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rakbow
 * @since 2022-08-20 2:54
 */
@AllArgsConstructor
@Getter
public enum ProductCategory {

    GAME(0, "enum.product_category.game"),
    ANIME_TV(1, "enum.product_category.anime_tv"),
    ANIME_MOVIE(2, "enum.product_category.anime_movie"),
    OVA_OAD(3, "enum.product_category.ova_oad"),
    NOVEL(4, "enum.product_category.novel"),
    MANGA(5, "enum.product_category.manga"),
    PUBLICATION(6, "enum.product_category.publication"),
    LIVE_ACTION_MOVIE(7, "enum.product_category.live_action_movie"),
    TV_SERIES(8, "enum.product_category.tv_series"),

    MISC(99, "enum.product_category.misc");

    @EnumValue
    private final Integer value;
    private final String labelKey;

    public static ProductCategory get(int value) {
        for (ProductCategory category : ProductCategory.values()) {
            if(category.value == value)
                return category;
        }
        return MISC;
    }

}
