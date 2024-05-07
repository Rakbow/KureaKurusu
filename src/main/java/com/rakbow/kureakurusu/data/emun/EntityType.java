package com.rakbow.kureakurusu.data.emun;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rakbow
 * @since 2024/4/12 2:23
 */
@Getter
@AllArgsConstructor
public enum EntityType {

    ITEM(0, "enum.entity.item"),
    SUBJECT(1, "enum.entity.subject"),
    PERSON(2, "enum.entity.person"),
    ROLE(3, "enum.entity.role"),
    EPISODE(4, "enum.entity.episode"),
    PRODUCT(99, "enum.entity.product"),
    FRANCHISE(100, "enum.entity.franchise");

    @EnumValue
    private final Integer value;
    private final String labelKey;
}
