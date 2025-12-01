package com.rakbow.kureakurusu.data.enums;

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
    ANIME(1, "enum.entry_sub_type.animation"),
    PRINT_PUBLICATION(2, "enum.entry_sub_type.print_publication"),
    ANIME_MOVIE(3, "enum.entry_sub_type.anime_movie"),
    GAME(4, "enum.entry_sub_type.game"),
    LIVE_ACTION(5, "enum.entry_sub_type.live_action"),
    TOKUSATSU(6, "enum.entry_sub_type.tokusatsu"),
    RADIO_AUDIO_DRAMA(7, "enum.entry_sub_type.radio_audio_drama"),
    MULTIMEDIA(8, "enum.entry_sub_type.multimedia"),
    COMPANY(9, "enum.entry_sub_type.company"),
    UNIT(10, "enum.entry_sub_type.unit"),
    STUDIO(11, "enum.entry_sub_type.studio"),
    FRANCHISE(100, "enum.entry_sub_type.franchise");

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
