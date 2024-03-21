package com.rakbow.kureakurusu.data.emun;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rakbow
 * @since 2024/3/3 23:56
 */
@Getter
@AllArgsConstructor
public enum Currency {

    JPY("JPY", "enum.currency.jpy"),
    CNY("CNY", "enum.currency.cny"),
    TWD("TWD", "enum.currency.twd"),
    EUR("EUR", "enum.currency.eur"),
    USD("USD", "enum.currency.usd");

    @EnumValue
    private final String value;
    private final String labelKey;

    public static Currency get(String value) {
        for (Currency category : Currency.values()) {
            if(category.value.equals(value))
                return category;
        }
        return JPY;
    }

}
