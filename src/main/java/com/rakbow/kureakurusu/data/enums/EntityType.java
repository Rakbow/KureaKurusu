package com.rakbow.kureakurusu.data.enums;

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

    ITEM(0, "enum.entity.item", "r2_item"),
    ENTRY(1, "enum.entity.entry", "r3_entry"),
    ROLE(3, "enum.entity.role", "r6_role"),
    EPISODE(4, "enum.entity.episode", "r7_episode"),
    ALBUM_DISC(5, "enum.default", "r7_disc"),
    INDEX(6, "enum.default", "r4_index"),
    FILE(98, "enum.entity.file", "r5_file_info"),
    USER(101, "enum.entity.user", "r1_sys_user");

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
