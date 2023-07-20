package com.rakbow.kureakurusu.data.emun.temp;

import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.util.common.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@AllArgsConstructor
public enum BookRelatedType {


    OTHER(0, "其他", "Other"),
    OFFPRINT(1, "单行本", "Offprint"),
    ADAPTATION(2, "改编", "Adaptation"),
    SERIES(3, "系列", "Series"),
    ART_BOOK(4, "设定集/画集", "Art Book"),
    PREQUEL(5, "前作", "Prequel"),
    SEQUEL(6, "续作", "Sequel"),
    SIDE_STORY(7, "番外篇", "Side Story"),
    PARENT_STORY(8, "主线故事", "Parent Story"),
    ALTERNATIVE_VERSION(9, "不同版本", "Alternative Version"),
    CHARACTER(10, "角色出演", "Character"),
    SAME_SETTING(11, "相同世界观", "Same Setting"),
    ALTERNATIVE_SETTING(12, "不同世界观", "Alternative Setting");

    @Getter
    private final int id;
    @Getter
    private final String nameZh;
    @Getter
    private final String nameEn;

}
