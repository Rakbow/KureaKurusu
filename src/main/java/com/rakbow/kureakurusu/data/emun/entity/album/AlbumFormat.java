package com.rakbow.kureakurusu.data.emun.entity.album;

import com.alibaba.fastjson2.JSON;
import com.rakbow.kureakurusu.data.Attribute;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2022-08-19 22:57
 * @Description: 专辑分类
 */
@AllArgsConstructor
public enum AlbumFormat {
    UNCATEGORIZED(0,"未分类", "Uncategorized"),
    VOCAL(1, "歌曲","Vocal"),
    OPENING_THEME(2, "片头曲","Opening Theme"),
    ENDING_THEME(3, "片尾曲","Ending Theme"),
    INSERT_SONG(4, "插入曲","Insert Song"),
    SOUNDTRACK(5, "原声","Soundtrack"),
    CHARACTER_SONG(6, "角色曲","Character Song"),
    DRAMA(7, "广播剧","Drama"),
    TALK(8, "广播电台","Talk"),
    REMIX(9, "混音","Remix"),
    DOUJIN_REMIX(10, "同人混音","Doujin Remix"),
    DERIVATIVE(11, "衍生曲","Derivative"),
    ARRANGEMENT(12, "改编","Arrangement"),
    DOUJIN_ARRANGEMENT(13,"同人改编","Doujin Arrangement"),
    VIDEO(14,"影片","Video");

    @Getter
    private final int id;
    @Getter
    private final String nameZh;
    @Getter
    private final String nameEn;

    public static List<String> getNamesByIds(List<Integer> ids) {
        return ids.stream().map(AlbumFormat::getNameById).collect(Collectors.toList());
    }

    public static List<Integer> getIdsByNames(List<String> names) {
        if (names.isEmpty()) return new ArrayList<>();
        return names.stream().map(AlbumFormat::getIdByName).collect(Collectors.toList());
    }

    public static String getNameById(int id) {
        for (AlbumFormat format : AlbumFormat.values()) {
            if (format.getId() == id) {
                if(StringUtils.equals(LocaleContextHolder.getLocale().getLanguage(), Locale.ENGLISH.getLanguage())) {
                    return format.getNameEn();
                }else {
                    return format.getNameZh();
                }
            }
        }
        return null;
    }

    public static int getIdByName(String name) {
        if(StringUtils.equals(LocaleContextHolder.getLocale().getLanguage(), Locale.ENGLISH.getLanguage())) {
            for (AlbumFormat format : AlbumFormat.values()) {
                if(StringUtils.equals(name, format.nameEn)) {
                    return format.id;
                }
            }
        }else {
            for (AlbumFormat format : AlbumFormat.values()) {
                if(StringUtils.equals(name, format.nameZh)) {
                    return format.id;
                }
            }
        }
        return 0;
    }

    public static List<Attribute> getAttributes(String json) {

        List<Attribute> res = new ArrayList<>();

        int[] ids = JSON.parseObject(json, int[].class);

        for(int id : ids) {
            res.add(new Attribute(id, getNameById(id)));
        }

        return res;
    }

}
