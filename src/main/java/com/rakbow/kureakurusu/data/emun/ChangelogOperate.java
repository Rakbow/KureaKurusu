package com.rakbow.kureakurusu.data.emun;

import com.baomidou.mybatisplus.annotation.EnumValue;
import io.github.linpeilie.annotations.AutoEnumMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rakbow
 * @since 2025/7/23 13:21
 */
@Getter
@AllArgsConstructor
@AutoEnumMapper("value")
public enum ChangelogOperate {

    CREATE(0, "enum.changelog_operate.create"),
    UPDATE(1, "enum.changelog_operate.update"),
    DELETE(2, "enum.changelog_operate.delete"),
    UPLOAD(3, "enum.changelog_operate.upload");

    @EnumValue
    private final Integer value;
    private final String labelKey;

    public static ChangelogOperate get(int value) {
        for (ChangelogOperate operate : ChangelogOperate.values()) {
            if (operate.value == value) return operate;
        }
        return CREATE;
    }
}