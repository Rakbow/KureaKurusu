package com.rakbow.kureakurusu.interceptor;

import com.rakbow.kureakurusu.data.RedisKey;
import com.rakbow.kureakurusu.data.auth.LoginUser;
import com.rakbow.kureakurusu.toolkit.CookieUtil;
import com.rakbow.kureakurusu.toolkit.JsonUtil;
import com.rakbow.kureakurusu.toolkit.RedisUtil;
import com.rakbow.kureakurusu.toolkit.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author Rakbow
 * @since 2022-08-17 23:25
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LoginTicketInterceptor implements HandlerInterceptor {

    private final RedisUtil redisUtil;

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        long startTime = System.currentTimeMillis();

        String path = request.getRequestURI();
        String method = request.getMethod();

        if (isPublicPath(path)) return true;

        //get ticket from cookie
        String ticket = CookieUtil.getValue(request, "ticket");
        if (StringUtil.isBlank(ticket)) return true;
        //get login ticket from redis
        String redisKey = STR."\{RedisKey.LOGIN_TICKET}\{ticket}";
        UserContextHolder.clear();

        try {

            if (!redisUtil.hasKey(redisKey)) return true;

            LoginUser user = JsonUtil.to(redisUtil.get(redisKey), LoginUser.class);

            // // ip address are changed, need login again
            // String ip = request.getHeader("X-Forwarded-For");
            // if (StringUtil.isNotBlank(ip)) ip = request.getRemoteAddr();
            // if (!StringUtil.equals(ip, user.getIpAddress())) {
            //     redisUtil.delete(redisKey);
            //     return true;
            // }
            //
            // // login by different remote, need login again
            // String userAgent = request.getHeader("User-Agent");
            // if (!StringUtil.equals(userAgent, user.getAgent())) {
            //     redisUtil.delete(redisKey);
            //     return true;
            // }

            // 在本次请求中持有用户
            UserContextHolder.setCurrentUser(user);
            // // 构建用户认证的结果，并存入securityContext，以便security进行授权
            // Authentication authentication = new UsernamePasswordAuthenticationToken(
            //         user);
            // SecurityContextHolder.setContext(new SecurityContextImpl(authentication));
            return true;
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            if (duration > 2000) log.warn("slow request: {} {} {}ms", method, path, duration);
        }
    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) {
        UserContextHolder.clear();
    }

    private boolean isPublicPath(String path) {
        return path.startsWith("/auth/login") ||
                path.startsWith("/auth/logout") ||
                path.startsWith("/auth/kaptcha");
    }

}
