package com.rakbow.kureakurusu.interceptor;

import com.rakbow.kureakurusu.data.auth.LoginUser;
import com.rakbow.kureakurusu.data.constant.RedisKey;
import com.rakbow.kureakurusu.toolkit.CookieUtil;
import com.rakbow.kureakurusu.toolkit.JsonUtil;
import com.rakbow.kureakurusu.toolkit.RedisUtil;
import com.rakbow.kureakurusu.toolkit.StringUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * @author Rakbow
 * @since 2026/3/28 17:13
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private final RedisUtil redisUtil;

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest req, @NotNull HttpServletResponse resp,
                                    @NotNull FilterChain chain) {

        long startTime = System.currentTimeMillis();

        String path = req.getRequestURI();
        String method = req.getMethod();

        try {

            String ticket = CookieUtil.getValue(req, "ticket");
            if (StringUtil.isBlank(ticket)) {
                SecurityContextHolder.clearContext();
                chain.doFilter(req, resp);
                return;
            }

            String redisKey = RedisKey.LOGIN_TICKET + ticket;

            UserContextHolder.clear();
            SecurityContextHolder.clearContext();

            if (!redisUtil.hasKey(redisKey)) {
                chain.doFilter(req, resp);
                return;
            }

            LoginUser user = JsonUtil.to(redisUtil.get(redisKey), LoginUser.class);

            UserContextHolder.setCurrentUser(user);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);

            chain.doFilter(req, resp);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            if (duration > 5000) {
                log.warn("slow request: {} {} {}ms", method, path, duration);
            }
            UserContextHolder.clear();
        }
    }
}
