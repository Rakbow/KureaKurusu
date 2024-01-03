package com.rakbow.kureakurusu.data.emun.common;

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
 * 媒体类型
 *
 * @author Rakbow
 * @since 2022-08-19 23:04
 */
@AllArgsConstructor
public enum MediaFormat {
    UNCATEGORIZED(0, "未分类", "Uncategorized"),
    CD(1, "CD", "CD"),
    DVD(2, "DVD", "DVD"),
    BLU_RAY(3, "Blu-ray", "Blu-ray"),
    DIGITAL(4, "数字专辑", "Digital");

    @Getter
    private final int id;
    @Getter
    private final String nameZh;
    @Getter
    private final String nameEn;

    /**
     * index列表转用逗号隔开的nameEn数组字符串
     *
     * @param ids index的JSONArray数组
     * @return String
     * @author rakbow
     */
    public static List<String> getNamesByIds(List<Integer> ids) {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        return ids.stream().map(id -> getNameById(id, lang)).collect(Collectors.toList());
    }

    public static List<Integer> getIdsByNames(List<String> names) {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        if (names.isEmpty()) return new ArrayList<>();
        return names.stream().map(name -> getIdByName(name, lang)).collect(Collectors.toList());
    }

    public static String getNameById(int id, String lang) {
        for (MediaFormat item : MediaFormat.values()) {
            if (item.getId() == id) {
                if(StringUtils.equals(lang, Locale.ENGLISH.getLanguage())) {
                    return item.getNameEn();
                }else {
                    return item.getNameZh();
                }
            }
        }
        return null;
    }

    public static int getIdByName(String name, String lang) {
        if(StringUtils.equals(lang, Locale.ENGLISH.getLanguage())) {
            for (MediaFormat format : MediaFormat.values()) {
                if(StringUtils.equals(name, format.nameEn)) {
                    return format.id;
                }
            }
        }else {
            for (MediaFormat format : MediaFormat.values()) {
                if(StringUtils.equals(name, format.nameZh)) {
                    return format.id;
                }
            }
        }
        return 0;
    }

    public static List<Attribute<Integer>> getAttributes(String json) {

        String lang = LocaleContextHolder.getLocale().getLanguage();

        List<Attribute<Integer>> res = new ArrayList<>();

        int[] ids = JSON.parseObject(json, int[].class);

        for(int id : ids) {
            res.add(new Attribute<Integer>(getNameById(id, lang), id));
        }

        return res;
    }

}
