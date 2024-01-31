package com.rakbow.kureakurusu.data.emun.temp;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rakbow
 * @since 2024/1/31 14:47
 */
@Getter
@AllArgsConstructor
public enum GameRelatedType {

    ADAPTATION(0, "enum.game_related_type.adaptation"),
    PREQUEL(0, "enum.game_related_type.prequel"),
    SEQUEL(0, "enum.game_related_type.sequel"),
    SIDE_STORY(0, "enum.game_related_type.side_story"),
    PARENT_STORY(0, "enum.game_related_type.parent_story"),
    SAME_SETTING(0, "enum.game_related_type.same_setting"),
    ALTERNATIVE_SETTING(0, "enum.game_related_type.alternative_setting"),
    ALTERNATIVE_VERSION(0, "enum.game_related_type.alternative_version"),
    CHARACTER(0, "enum.game_related_type.character"),
    COLLABORATION(0, "enum.game_related_type.collaboration"),
    DLC(0, "enum.game_related_type.dlc"),
    OTHER(0, "enum.game_related_type.other");

    @EnumValue
    private final Integer value;
    private final String labelKey;

}