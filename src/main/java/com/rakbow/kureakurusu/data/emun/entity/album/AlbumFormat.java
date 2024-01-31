package com.rakbow.kureakurusu.data.emun.entity.album;

import com.baomidou.mybatisplus.annotation.EnumValue;
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
public enum AlbumFormat {

    UNCATEGORIZED(0,"enum.album_format.uncategorized"),
    VOCAL(1, "enum.album_format.vocal"),
    OPENING_THEME(2, "enum.album_format.opening_theme"),
    ENDING_THEME(3, "enum.album_format.ending_theme"),
    INSERT_SONG(4, "enum.album_format.insert_song"),
    SOUNDTRACK(5, "enum.album_format.soundtrack"),
    CHARACTER_SONG(6, "enum.album_format.character_song"),
    DRAMA(7, "enum.album_format.drama"),
    TALK(8, "enum.album_format.talk"),
    REMIX(9, "enum.album_format.Remix"),
    DOUJIN_REMIX(10, "enum.album_format.Doujin_Remix"),
    DERIVATIVE(11, "enum.album_format.Derivative"),
    ARRANGEMENT(12, "enum.album_format.Arrangement"),
    DOUJIN_ARRANGEMENT(13,"enum.album_format.Doujin_Arrangement"),
    VIDEO(14,"enum.album_format.Video");

    @EnumValue
    private final Integer value;
    private final String labelKey;

    public static AlbumFormat get(int value) {
        for (AlbumFormat format : AlbumFormat.values()) {
            if(format.value == value)
                return format;
        }
        return UNCATEGORIZED;
    }

}
