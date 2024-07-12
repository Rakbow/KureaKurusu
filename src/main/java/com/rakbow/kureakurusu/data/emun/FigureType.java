package com.rakbow.kureakurusu.data.emun;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rakbow
 * @since 2024/7/6 6:24
 */
@Getter
@AllArgsConstructor
public enum FigureType {

    PREPAINTED(0, "enum.figure_type.prepainted"),
    ACTION_DOLLS(1, "enum.figure_type.action_dolls"),
    TRADING(2, "enum.figure_type.trading"),
    ACCESSORIES(3, "enum.figure_type.accessories"),
    MODEL_KITS(4, "enum.figure_type.model_kits"),
    GARAGE_KITS(5, "enum.figure_type.garage_kits");

    @EnumValue
    private final Integer value;
    private final String labelKey;

}
