package com.rakbow.kureakurusu.data.emun;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author Rakbow
 * @since 2022-08-13 22:16
 */
@Getter
@AllArgsConstructor
public enum EntityType {

    ITEM(0, "enum.entity.item", "item"),
    ENTRY(1, "enum.entity.entry", "entry"),
    ROLE(3, "enum.entity.role", "role"),
    EPISODE(4, "enum.entity.episode", "episode"),
    ALBUM_DISC(5, "enum.entity.default", "album_disc"),
    FILE(98, "enum.entity.file", "file_info"),
    USER(101, "enum.entity.user", "user");

    @EnumValue
    private final Integer value;
    private final String labelKey;
    private final String tableName;

    public static EntityType get(int value) {
        for (EntityType type : EntityType.values())
            if (type.getValue() == value) return type;
        return ITEM;
    }

    public static String getTableName(int value) {
        return Arrays.stream(EntityType.values())
                .filter(entity -> entity.getValue() == value)
                .findFirst()
                .map(EntityType::getTableName)
                .orElse(null);
    }

}
