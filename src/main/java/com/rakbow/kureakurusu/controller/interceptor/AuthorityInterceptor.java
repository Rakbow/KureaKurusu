package com.rakbow.kureakurusu.controller.interceptor;

import com.rakbow.kureakurusu.annotation.AdminAuthorityRequired;
import com.rakbow.kureakurusu.data.emun.system.UserAuthority;
import com.rakbow.kureakurusu.entity.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author Rakbow
 * @since 2022-09-30 9:51
 */
public class AuthorityInterceptor implements HandlerInterceptor {

    private static final ThreadLocal<User> CURRENT_USER = new ThreadLocal<>();

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            AdminAuthorityRequired adminAuthorityRequired = method.getAnnotation(AdminAuthorityRequired.class);
            // response.;
            return adminAuthorityRequired == null || CURRENT_USER.get() != null;
        }
        return true;
    }

    public static User getCurrentUser() {
        return CURRENT_USER.get();
    }

    public static void setCurrentUser(User user) {
        CURRENT_USER.set(user);
    }

    public static void clearCurrentUser() {
        CURRENT_USER.remove();
    }

    public static boolean isUser() {
        return UserAuthority.isUser(CURRENT_USER.get());
    }

    public static boolean isJunior() {
        return UserAuthority.isJunior(CURRENT_USER.get());
    }

    public static boolean isSenior() {
        return UserAuthority.isSenior(CURRENT_USER.get());
    }

    public static boolean isAdmin() {
        return UserAuthority.isAdmin(CURRENT_USER.get());
    }

}
