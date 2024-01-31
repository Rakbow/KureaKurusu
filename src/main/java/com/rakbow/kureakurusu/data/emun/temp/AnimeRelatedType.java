package com.rakbow.kureakurusu.data.emun.temp;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rakbow
 * @since 2024/1/31 11:36
 */
@Getter
@AllArgsConstructor
public enum AnimeRelatedType {

    OTHER(0, "enum.anime_related_type.other"),
    ADAPTATION(0, "enum.anime_related_type.adaptation"),
    PREQUEL(0, "enum.anime_related_type.prequel"),
    SEQUEL(0, "enum.anime_related_type.sequel"),
    SUMMARY(0, "enum.anime_related_type.summary"),
    FULL_STORY(0, "enum.anime_related_type.full_story"),
    SIDE_STORY(0, "enum.anime_related_type.side_story"),
    SAME_SETTING(0, "enum.anime_related_type.same_setting"),
    ALTERNATIVE_SETTING(0, "enum.anime_related_type.alternative_setting"),
    ALTERNATIVE_VERSION(0, "enum.anime_related_type.alternative_version"),
    SPIN_OFF(0, "enum.anime_related_type.spin_off"),
    PARENT_STORY(0, "enum.anime_related_type.parent_story"),
    CHARACTER(0, "enum.anime_related_type.character"),
    COLLABORATION(0, "enum.anime_related_type.collaboration");

    @EnumValue
    private final Integer value;
    private final String labelKey;

}
