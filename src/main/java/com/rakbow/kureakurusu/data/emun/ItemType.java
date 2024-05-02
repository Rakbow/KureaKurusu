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

    ALBUM(1, "enum.entity.album"),
    BOOK(2, "enum.entity.book"),
    DISC(3, "enum.entity.disc"),
    GAME(4, "enum.entity.game"),
    GOODS(5, "enum.entity.goods"),
    FIGURE(6, "enum.entity.figure");

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