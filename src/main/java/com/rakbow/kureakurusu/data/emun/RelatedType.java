package com.rakbow.kureakurusu.data.emun;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rakbow
 * @since 2024/2/28 11:22
 */
@Getter
@AllArgsConstructor
public enum RelatedType {

    OTHER(0, "enum.related_type.other"),
    MAIN_ENTRY(1, "enum.related_type.main_entry"),
    PREQUEL(2, "enum.related_type.prequel"),
    SEQUEL(3, "enum.related_type.sequel"),
    PARENT_STORY(4, "enum.related_type.parent_story"),
    SIDE_STORY(5, "enum.related_type.side_story"),
    ADAPTATION(6, "enum.related_type.adaptation"),
    SAME_SETTING(7, "enum.related_type.same_setting"),
    ALTERNATIVE_SETTING(8, "enum.related_type.alternative_setting"),
    ALTERNATIVE_VERSION(9, "enum.related_type.alternative_version"),
    CHARACTER(10, "enum.related_type.character"),
    SPIN_OFF(11, "enum.related_type.spin_off"),
    COLLABORATION(12, "enum.related_type.collaboration"),
    OFFPRINT(13, "enum.related_type.offprint"),
    ART_BOOK(14, "enum.related_type.art_book"),
    DLC(15, "enum.related_type.dlc"),
    OST(16, "enum.related_type.ost"),
    CHARACTER_SONG(17, "enum.related_type.character_song"),
    OPENING_SONG(18, "enum.related_type.opening_song"),
    ENDING_SONG(19, "enum.related_type.ending_song"),
    INSERT_SONG(20, "enum.related_type.insert_song"),
    IMAGE_SONG(21, "enum.related_type.image_song"),
    DRAMA(22, "enum.related_type.drama");

    @EnumValue
    private final Integer value;
    private final String labelKey;

    public static RelatedType get(int value) {
        for (RelatedType type : RelatedType.values()) {
            if(type.value == value)
                return type;
        }
        return OTHER;
    }

}
