package com.rakbow.kureakurusu.toolkit;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author Rakbow
 * @since 2022-08-17 23:27
 */
public class CookieUtil {

    public static String getValue(HttpServletRequest request, String name) {
        if (request == null || name == null)
            throw new IllegalArgumentException(I18nHelper.getMessage("system.illegal_argument"));
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) return cookie.getValue();
            }
        }
        return null;
    }

}
