package com.rakbow.kureakurusu.toolkit;

import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Arrays;
import java.util.Locale;

/**
 * @author Rakbow
 * @since 2023-05-18 20:40
 */
public class LocaleUtil {

    private static final String[] SUPPORTED_LOCALE = {"zh", "en"};

    public static boolean isZh() {

        String lang = LocaleContextHolder.getLocale().getLanguage();
        if(!Arrays.asList(SUPPORTED_LOCALE).contains(lang)) return true;
        return StringUtil.equals(lang, Locale.CHINESE.getLanguage());
    }

    public static boolean isZh(String lang) {
        return StringUtil.equals(lang, Locale.CHINESE.getLanguage());
    }

    public static boolean isEn() {
        return StringUtil.equals(LocaleContextHolder.getLocale().getLanguage(), Locale.ENGLISH.getLanguage());
    }

    public static boolean isEn(String lang) {
        return StringUtil.equals(lang, Locale.ENGLISH.getLanguage());
    }

}
