package com.rakbow.kureakurusu.interceptor;

import com.rakbow.kureakurusu.data.auth.LoginUser;

import java.util.Objects;

/**
 * @author Rakbow
 * @since 2022-09-30 9:51
 */
public class UserContextHolder {

    private static final ThreadLocal<LoginUser> CURRENT_USER = new ThreadLocal<>();

    private UserContextHolder() {}

    public static void setCurrentUser(LoginUser user) {
        CURRENT_USER.set(user);
    }

    public static LoginUser getCurrentUser() {
        return CURRENT_USER.get();
    }

    public static boolean isLogin() {
        return Objects.nonNull(CURRENT_USER.get());
    }

    public static void clear() {
        CURRENT_USER.remove();
    }

}
