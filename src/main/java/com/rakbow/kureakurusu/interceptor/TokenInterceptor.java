package com.rakbow.kureakurusu.interceptor;

import com.rakbow.kureakurusu.toolkit.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
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

    @SneakyThrows
    @Override
    public boolean preHandle(@NotNull HttpServletRequest req, @NotNull HttpServletResponse resp,
                             @NotNull Object handler) {
        VISIT_TOKEN.set(CookieUtil.getValue(req, "visit_token"));
        LIKE_TOKEN.set(CookieUtil.getValue(req, "like_token"));
        return HandlerInterceptor.super.preHandle(req, resp, handler);
    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest req, @NotNull HttpServletResponse resp,
                                @NotNull Object handler, Exception ex) {
        VISIT_TOKEN.remove();
        LIKE_TOKEN.remove();
    }

    public static String getLikeToken() {
        return LIKE_TOKEN.get();
    }

    public static String getVisitToken() {
        return VISIT_TOKEN.get();
    }

}
