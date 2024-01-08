package com.rakbow.kureakurusu.util;

import com.rakbow.kureakurusu.util.common.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * @author Rakbow
 * @since 2023-11-05 2:59
 */
public class I18nHelper {

    private static MessageSource messageSource;

    public static void init() {
        if(messageSource == null){
            messageSource = SpringUtil.getMessageSource();
        }
    }

    public static String getMessage(String key) {
        init();
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, null, locale);
    }

    public static String getMessage(String key, Locale locale) {
        init();
        return messageSource.getMessage(key, null, locale);
    }

    public static String getMessage(String key, String... args) {
        init();
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, args, locale);
    }

}
