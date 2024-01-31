package com.rakbow.kureakurusu.data.emun.temp;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.util.common.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Getter
@AllArgsConstructor
public enum BookRelatedType {

    OTHER(0, "enum.book_related_type.other"),
    OFFPRINT(1, "enum.book_related_type.offprint"),
    ADAPTATION(2, "enum.book_related_type.adaptation"),
    SERIES(3, "enum.book_related_type.series"),
    ART_BOOK(4, "enum.book_related_type.art_book"),
    PREQUEL(5, "enum.book_related_type.prequel"),
    SEQUEL(6, "enum.book_related_type.sequel"),
    SIDE_STORY(7, "enum.book_related_type.side_story"),
    PARENT_STORY(8, "enum.book_related_type.parent_story"),
    ALTERNATIVE_VERSION(9, "enum.book_related_type.alternative_version"),
    CHARACTER(10, "enum.book_related_type.character"),
    SAME_SETTING(11, "enum.book_related_type.same_setting"),
    ALTERNATIVE_SETTING(12, "enum.book_related_type.alternative_setting");

    @EnumValue
    private final Integer value;
    private final String labelKey;

}
