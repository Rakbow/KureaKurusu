package com.rakbow.kureakurusu.data.emun;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rakbow
 * @since 2024/7/6 6:07
 */
@Getter
@AllArgsConstructor
public enum GoodsType {

    MISC(0, "enum.goods_type.misc"),
    HANGED_UP(1, "enum.goods_type.hanged_up"),
    PLUSH_TOY(2, "enum.goods_type.plush_toy"),
    STATIONERY(3, "enum.goods_type.stationery"),
    DISHES(4, "enum.goods_type.dishes"),
    APPAREL(5, "enum.goods_type.apparel"),
    LINENS(6, "enum.goods_type.linens"),
    ON_WALLS(7, "enum.goods_type.on_walls");

    @EnumValue
    private final Integer value;
    private final String labelKey;

}
