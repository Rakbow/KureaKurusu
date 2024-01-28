package com.rakbow.kureakurusu.interceptor;

import com.rakbow.kureakurusu.util.common.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author Rakbow
 * @since 2023-05-06 17:25
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {

    private static final ThreadLocal<String> VISIT_TOKEN = new ThreadLocal<>();
    private static final ThreadLocal<String> LIKE_TOKEN = new ThreadLocal<>();

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler)
            throws Exception {
        VISIT_TOKEN.set(CookieUtil.getValue(request, "visit_token"));
        LIKE_TOKEN.set(CookieUtil.getValue(request, "like_token"));
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    public static String getLikeToken() {
        return LIKE_TOKEN.get();
    }

    public static String getVisitToken() {
        return VISIT_TOKEN.get();
    }

}
