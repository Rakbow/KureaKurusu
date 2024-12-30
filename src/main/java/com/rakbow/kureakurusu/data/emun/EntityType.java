package com.rakbow.kureakurusu.data.emun;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
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
    ENTRY(1, "enum.entity.subject", "entry"),
    PERSON(2, "enum.entity.person", "person"),
    ROLE(3, "enum.entity.role", "role"),
    EPISODE(4, "enum.entity.episode", "episode"),
    CHARACTER(5, "enum.entity.character", "chara"),
    PRODUCT(99, "enum.entity.product", "product"),
    FRANCHISE(100, "enum.entity.franchise", "franchise"),
    USER(101, "enum.entity.user", "user");

    @EnumValue
    private final Integer value;
    private final String labelKey;
    private final String tableName;

    public String getLabel() {
        return I18nHelper.getMessage(this.labelKey);
    }

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
