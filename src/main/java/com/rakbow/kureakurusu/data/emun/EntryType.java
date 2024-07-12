package com.rakbow.kureakurusu.data.emun;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rakbow
 * @since 2024/6/18 18:05
 */
@Getter
@AllArgsConstructor
public enum EntryType {

    CLASSIFICATION(1, "enum.entry_type.classification"),
    MATERIAL(2, "enum.entry_type.material"),
    EVENT(3, "enum.entry_type.event");

    @EnumValue
    private final Integer value;
    private final String labelKey;

}
