package com.rakbow.kureakurusu.service;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Locale;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-10-31 2:09
 * @Description:
 */
@Service
public class I18nService {

    @Resource
    private final MessageSource messageSource;

    public I18nService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String key) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, null, locale);
    }

    public String getMessage(String key, String... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, args, locale);
    }

}
