package com.rakbow.kureakurusu.data.emun;

import com.baomidou.mybatisplus.annotation.EnumValue;
import io.github.linpeilie.annotations.AutoEnumMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rakbow
 * @since 2024-04-25 10:00
 */
@Getter
@AllArgsConstructor
@AutoEnumMapper("value")
public enum ItemType {

    ALBUM(1, "enum.item.album"),
    BOOK(2, "enum.item.book"),
    DISC(3, "enum.item.disc"),
    GAME(4, "enum.item.game"),
    GOODS(5, "enum.item.goods"),
    FIGURE(6, "enum.item.figure");

    @EnumValue
    private final Integer value;
    private final String labelKey;

    public static ItemType get(int value) {
        for (ItemType type : ItemType.values()) {
            if(type.value == value) return type;
        }
        return ALBUM;
    }
}