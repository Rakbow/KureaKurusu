package com.rakbow.kureakurusu.data.emun.temp;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rakbow
 * @since 2024/1/31 14:41
 */
@Getter
@AllArgsConstructor
public enum AlbumRelatedType {

    OST(0, "enum.album_related_type.ost"),
    CHARACTER_SONG(0, "enum.album_related_type.character_song"),
    OPENING_SONG(0, "enum.album_related_type.opening_song"),
    ENDING_SONG(0, "enum.album_related_type.ending_song"),
    INSERT_SONG(0, "enum.album_related_type.insert_song"),
    IMAGE_SONG(0, "enum.album_related_type.image_song"),
    drama(0, "enum.album_related_type.drama"),
    OTHER(0, "enum.album_related_type.other");

    @EnumValue
    private final Integer value;
    private final String labelKey;

}
