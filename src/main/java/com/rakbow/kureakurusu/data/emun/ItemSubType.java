package com.rakbow.kureakurusu.data.emun;

import com.baomidou.mybatisplus.annotation.EnumValue;
import io.github.linpeilie.annotations.AutoEnumMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rakbow
 * @since 2024/12/29 19:52
 */
@Getter
@AllArgsConstructor
@AutoEnumMapper("value")
public enum ItemSubType {

    DEFAULT(0, "enum.default"),

    MISC(1, "enum.item_sub_type.misc"),
    HANGED_UP(2, "enum.item_sub_type.hanged_up"),
    PLUSH_TOY(3, "enum.item_sub_type.plush_toy"),
    STATIONERY(4, "enum.item_sub_type.stationery"),
    DISHES(5, "enum.item_sub_type.dishes"),
    APPAREL(6, "enum.item_sub_type.apparel"),
    LINENS(7, "enum.item_sub_type.linens"),
    ON_WALLS(8, "enum.item_sub_type.on_walls"),

    PREPAINTED(9, "enum.item_sub_type.prepainted"),
    ACTION_DOLLS(10, "enum.item_sub_type.action_dolls"),
    TRADING(11, "enum.item_sub_type.trading"),
    ACCESSORIES(12, "enum.item_sub_type.accessories"),
    MODEL_KITS(13, "enum.item_sub_type.model_kits"),
    GARAGE_KITS(14, "enum.item_sub_type.garage_kits"),

    NOVEL(15,"enum.item_sub_type.novel"),
    COMIC(16,"enum.item_sub_type.manga"),
    ANTHOLOGY(17,"enum.item_sub_type.anthology"),
    ART_BOOK(18,"enum.item_sub_type.art_book"),
    ELECTRONIC_BOOK(19,"enum.item_sub_type.e_book");

    @EnumValue
    private final Integer value;
    private final String labelKey;

    public static ItemSubType get(int value) {
        for (ItemSubType type : ItemSubType.values()) {
            if(type.value == value) return type;
        }
        return DEFAULT;
    }


}
