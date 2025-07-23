package com.rakbow.kureakurusu.data.emun;

import com.baomidou.mybatisplus.annotation.EnumValue;
import io.github.linpeilie.annotations.AutoEnumMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rakbow
 * @since 2025/6/10 14:38
 */
@Getter
@AllArgsConstructor
@AutoEnumMapper("value")
public enum EntrySubType {

    DEFAULT(0, "enum.default"),
    ANIME(1, "enum.entry_sub_type.anime"),
    NOVEL(2, "enum.entry_sub_type.novel"),
    COMIC(3, "enum.entry_sub_type.manga"),
    GAME(4, "enum.entry_sub_type.game"),
    MOVIE(5, "enum.entry_sub_type.movie"),
    TV_SERIES(6, "enum.entry_sub_type.tv_series"),
    MAIN_SERIES(100, "enum.entry_sub_type.main_series");

    @EnumValue
    private final Integer value;
    private final String labelKey;

    public static EntrySubType get(int value) {
        for (EntrySubType type : EntrySubType.values()) {
            if(type.value == value)
                return type;
        }
        return DEFAULT;
    }

}
