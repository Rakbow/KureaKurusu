package com.rakbow.kureakurusu.data.emun;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rakbow
 * @since 2022-12-29 21:30
 */
@Getter
@AllArgsConstructor
public enum Language {

    JAPANESE("ja-JP", "enum.language.japanese"),
    SIMPLIFIED_CHINESE("zh-CN", "enum.language.simplified_chinese"),
    TRADITIONAL_CHINESE("zh-TW", "enum.language.traditional_chinese"),
    ENGLISH("en-US", "enum.language.english");

    @EnumValue
    private final String value;
    private final String labelKey;

    public static Language get(String value) {
        for (Language lang : Language.values()) {
            if(lang.value.equals(value))
                return lang;
        }
        return JAPANESE;
    }

}
