package com.rakbow.kureakurusu.interceptor;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import jakarta.servlet.http.HttpServletRequest;

import java.time.Duration;
import java.util.List;
import java.util.Locale;

/**
 * @author Rakbow
 * @since 2023-04-29 21:54 get locate from cookie
 */
public class LocaleResolver extends CookieLocaleResolver {

    public static final Locale DEFAULT_LOCALE = Locale.CHINA;

    public static final List<String> SUPPORTED_LOCALES = List.of("en", "zh");

    @NotNull
    @Override
    public Locale resolveLocale(@NotNull HttpServletRequest request) {
        Locale locale = super.resolveLocale(request);

        if (!SUPPORTED_LOCALES.contains(locale.getLanguage())) {
            locale = DEFAULT_LOCALE;
        }

        LocaleContextHolder.setLocale(locale);
        return locale;
    }

    //重写构造方法,改变cookie信息
    public LocaleResolver() {
        super("lang");
        super.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        //cookie有效期30天
        this.setCookieMaxAge(Duration.ofSeconds(30 * 24 * 60 * 60));
    }

}
