package com.rakbow.kureakurusu.data.emun;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author Rakbow
 * @since 2024/4/12 2:23
 */
@Getter
@AllArgsConstructor
public enum EntityType {

    ITEM(0, "enum.entity.item", "item"),
    SUBJECT(1, "enum.entity.subject", "subject"),
    PERSON(2, "enum.entity.person", "person"),
    ROLE(3, "enum.entity.role", "person_role"),
    EPISODE(4, "enum.entity.episode", "episode"),
    PRODUCT(99, "enum.entity.product", "product"),
    FRANCHISE(100, "enum.entity.franchise", "franchise");

    @EnumValue
    private final Integer value;
    private final String labelKey;
    private final String tableName;

    public static String getTableName(int value) {
        return Arrays.stream(EntityType.values())
                .filter(entity -> entity.getValue() == value)
                .findFirst()
                .map(EntityType::getTableName)
                .orElse(null);
    }

}
