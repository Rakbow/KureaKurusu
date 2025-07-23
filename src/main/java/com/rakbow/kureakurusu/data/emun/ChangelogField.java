package com.rakbow.kureakurusu.data.emun;

import com.baomidou.mybatisplus.annotation.EnumValue;
import io.github.linpeilie.annotations.AutoEnumMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rakbow
 * @since 2025/7/23 13:42
 */
@Getter
@AllArgsConstructor
@AutoEnumMapper("value")
public enum ChangelogField {

    DEFAULT(0, "enum.default"),
    BASIC(1, "enum.changelog_field.basic"),
    IMAGE(2, "enum.changelog_field.image"),
    FILE(3, "enum.changelog_field.file"),
    EPISODE(4, "enum.changelog_field.episode"),
    PRODUCT(5, "enum.changelog_field.product"),
    PERSON(6, "enum.changelog_field.person"),
    CHARACTER(7, "enum.changelog_field.character"),
    CLASS(8, "enum.changelog_field.classification"),
    MATERIAL(9, "enum.changelog_field.material"),
    EVENT(10, "enum.changelog_field.event"),
    DETAIL(11, "enum.changelog_field.detail");

    @EnumValue
    private final Integer value;
    private final String labelKey;

    public static ChangelogField get(int value) {
        for (ChangelogField field : ChangelogField.values()) {
            if (field.value == value) return field;
        }
        return DEFAULT;
    }

    public static ChangelogField getByEntryType(int entryType) {

        EntryType subType = EntryType.get(entryType);
        return switch (subType) {
            case PRODUCT -> ChangelogField.PRODUCT;
            case PERSON -> ChangelogField.PERSON;
            case CHARACTER -> ChangelogField.CHARACTER;
            case CLASSIFICATION -> ChangelogField.CLASS;
            case MATERIAL -> ChangelogField.MATERIAL;
            case EVENT -> ChangelogField.EVENT;
        };
    }

}
