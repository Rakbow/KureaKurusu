package com.rakbow.kureakurusu.data.meta;

import org.springframework.context.i18n.LocaleContextHolder;

/**
 * @author Rakbow
 * @since 2023-11-05 5:17
 */
public class MetaData {

    public static MetaOption optionsZh;
    public static MetaOption optionsEn;

    public static MetaOption getOptions() {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        if(lang.equals("en"))
            return optionsEn;
        return optionsZh;
    }
}
