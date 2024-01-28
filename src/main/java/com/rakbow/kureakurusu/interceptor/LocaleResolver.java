package com.rakbow.kureakurusu.interceptor;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * @author Rakbow
 * @since 2023-04-29 21:54 get locate from cookie
 */
public class LocaleResolver extends CookieLocaleResolver {

    public static final Locale DEFAULT_LOCALE = Locale.CHINA;

    public static final String[] SUPPORTED_LOCALES = {"en", "zh"};

    @NotNull
    @Override
    public Locale resolveLocale(@NotNull HttpServletRequest request) {
        Locale locale = super.resolveLocale(request);

        if (!isSupported(locale)) {
            locale = DEFAULT_LOCALE;
        }

        LocaleContextHolder.setLocale(locale);
        return locale;
    }

    private boolean isSupported(Locale locale) {
        for (String supportedLocale : SUPPORTED_LOCALES) {
            if (supportedLocale.equals(locale.getLanguage())) {
                return true;
            }
        }
        return false;
    }

    //重写构造方法,改变cookie信息
    public LocaleResolver(){
        this.setCookieName("locale");
        //cookie有效期30天
        this.setCookieMaxAge(30*24*60*60);
    }

}
