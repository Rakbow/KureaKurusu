package com.rakbow.kureakurusu.interceptor;

import com.rakbow.kureakurusu.data.entity.User;
import com.rakbow.kureakurusu.service.UserService;
import com.rakbow.kureakurusu.util.common.CookieUtil;
import com.rakbow.kureakurusu.util.common.RedisUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import static com.rakbow.kureakurusu.data.common.Constant.RISK;

/**
 * @author Rakbow
 * @since 2022-08-17 23:25
 */
@Component
public class LoginTicketInterceptor implements HandlerInterceptor {

    @Resource
    private UserService userSrv;
    @Resource
    private RedisUtil redisUtil;

    //每次请求前
    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {

        //get ticket from cookie
        String ticket = CookieUtil.getValue(request, "ticket");
        if (StringUtils.isBlank(ticket)) return true;
        User user = null;
        //get login ticket from redis
        String redisTicketKey = STR."login_ticket\{RISK}\{ticket}";
        if (redisUtil.hasKey(redisTicketKey)) {
            long userId = Long.parseLong(redisUtil.get(redisTicketKey).toString());
            if ((AuthorityInterceptor.isCurrentUser() && AuthorityInterceptor.getCurrentUser().getId() != userId)
                    || !AuthorityInterceptor.isCurrentUser()) {
                user = userSrv.getById(userId);
                AuthorityInterceptor.clearCurrentUser();
                // 在本次请求中持有用户
                AuthorityInterceptor.setCurrentUser(user);
            }
            if(user == null) user = AuthorityInterceptor.getCurrentUser();
            // 构建用户认证的结果，并存入securityContext，以便security进行授权
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    user, user.getPassword(), AuthorityInterceptor.getAuthorities(user));
            SecurityContextHolder.setContext(new SecurityContextImpl(authentication));
        } else {
            // 凭证失效
            AuthorityInterceptor.clearCurrentUser();
        }
        return true;
    }

    @Override
    public void postHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, ModelAndView modelAndView) {
        User user = AuthorityInterceptor.getCurrentUser();
        if (user != null && modelAndView != null) {
            modelAndView.addObject("loginUser", user);
        }
    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) {
        AuthorityInterceptor.clearCurrentUser();
    }

}
