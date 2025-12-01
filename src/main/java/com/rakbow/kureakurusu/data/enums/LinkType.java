package com.rakbow.kureakurusu.data.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import io.github.linpeilie.annotations.AutoEnumMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rakbow
 * @since 2025/11/21 23:40
 */
@Getter
@AllArgsConstructor
@AutoEnumMapper("value")
public enum LinkType {

    DEFAULT(0, "enum.default"),
    AVAILABLE(1, "enum.link.available"),
    OFFICIAL(2, "enum.link.official"),
    PERSONAL(3, "enum.link.personal"),
    REFERENCE(4, "enum.link.reference"),
    FANSITE(5, "enum.link.fansite"),
    INTERVIEW(6, "enum.link.interview"),
    REVIEW(7, "enum.link.review"),
    COMMERCIAL(8, "enum.link.commercial");

    @EnumValue
    private final Integer value;
    private final String labelKey;

    public static LinkType get(int value) {
        for (LinkType type : LinkType.values()) {
            if(type.value == value) return type;
        }
        return DEFAULT;
    }

}
