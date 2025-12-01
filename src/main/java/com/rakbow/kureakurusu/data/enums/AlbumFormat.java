package com.rakbow.kureakurusu.data.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import io.github.linpeilie.annotations.AutoEnumMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 专辑分类
 *
 * @author Rakbow
 * @since 2022-08-19 22:57
 */
@Getter
@AllArgsConstructor
@AutoEnumMapper("value")
public enum AlbumFormat {

    VOCAL(1, "enum.album_format.vocal"),
    ORIGINAL_SOUNDTRACK(2, "enum.album_format.original_soundtrack"),
    ARRANGEMENT(3, "enum.album_format.arrangement"),
    DRAMA(4, "enum.album_format.drama"),
    LIVE_EVENT(5, "enum.album_format.live_event"),
    REMIX(6, "enum.album_format.remix"),
    ORIGINAL_WORK(7, "enum.album_format.original_work"),
    TALK(8, "enum.album_format.talk"),
    REMASTER(9, "enum.album_format.remaster"),
    PROTOTYPE_UNUSED(10, "enum.album_format.prototype_unused"),
    SOUND_EFFECT(11, "enum.album_format.sound_effect"),
    DATA(12, "enum.album_format.data"),
    VIDEO(13, "enum.album_format.video");

    @EnumValue
    private final Integer value;
    private final String labelKey;

    public static AlbumFormat get(int value) {
        for (AlbumFormat format : AlbumFormat.values()) {
            if(format.value == value)
                return format;
        }
        return VOCAL;
    }

}
