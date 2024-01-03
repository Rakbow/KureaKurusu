package com.rakbow.kureakurusu.data.emun;

import com.rakbow.kureakurusu.data.Attribute;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Rakbow
 * @since 2023-05-19 19:02
 */
public interface MetaEnum {

    int getId();
    String getNameZh();
    String getNameEn();

    static <T extends Enum<T> & MetaEnum> String getLocaleNameById(Class<T> enumClass, int id) {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        for (T e : enumClass.getEnumConstants()) {
            if (e.getId() == id) {
                if(StringUtils.equals(lang, Locale.ENGLISH.getLanguage())) {
                    return e.getNameEn();
                }else {
                    return e.getNameZh();
                }
            }
        }
        return "Uncategorized";
    }

    static <T extends Enum<T> & MetaEnum> List<Attribute<Integer>> getAttributeSet(Class<T> enumClass, String lang) {
        List<Attribute<Integer>> set = new ArrayList<>();
        for (T e : enumClass.getEnumConstants()) {
            if(StringUtils.equals(lang, Locale.CHINESE.getLanguage())) {
                set.add(new Attribute<Integer>(e.getNameZh(), e.getId()));
            }else if(StringUtils.equals(lang, Locale.ENGLISH.getLanguage())) {
                set.add(new Attribute<Integer>(e.getNameEn(), e.getId()));
            }
        }
        return set;
    }

}
