package com.rakbow.kureakurusu.interceptor;

import com.rakbow.kureakurusu.annotation.AdminAuthorityRequired;
import com.rakbow.kureakurusu.data.emun.UserAuthority;
import com.rakbow.kureakurusu.data.entity.User;
import com.rakbow.kureakurusu.util.I18nHelper;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Rakbow
 * @since 2022-09-30 9:51
 */
public class AuthorityInterceptor implements HandlerInterceptor {

    private static final ThreadLocal<User> CURRENT_USER = new ThreadLocal<>();

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        if (handler instanceof HandlerMethod handlerMethod) {
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

    public static boolean isCurrentUser() {
        return CURRENT_USER.get() == null;
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

    public static Collection<? extends GrantedAuthority> getAuthorities(User user) {
        List<GrantedAuthority> list = new ArrayList<>();
        switch (user.getType()) {
            case UserAuthority.ADMIN -> list.add(new SimpleGrantedAuthority(STR."ROLE_\{I18nHelper.getMessage(UserAuthority.ADMIN.getLabelKey(), "en")}"));
            case UserAuthority.USER -> list.add(new SimpleGrantedAuthority(STR."ROLE_\{I18nHelper.getMessage(UserAuthority.USER.getLabelKey(), "en")}"));
            case UserAuthority.JUNIOR_EDITOR -> list.add(new SimpleGrantedAuthority(STR."ROLE_\{I18nHelper.getMessage(UserAuthority.JUNIOR_EDITOR.getLabelKey(), "en")}"));
            case UserAuthority.SENIOR_EDITOR -> list.add(new SimpleGrantedAuthority(STR."ROLE_\{I18nHelper.getMessage(UserAuthority.SENIOR_EDITOR.getLabelKey(), "en")}"));
        }
        return list;
    }

}
