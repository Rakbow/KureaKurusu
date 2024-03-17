package com.rakbow.kureakurusu.data.emun.system;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.rakbow.kureakurusu.data.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rakbow
 * @since 2023-02-10 21:01
 */
@Getter
@AllArgsConstructor
public enum UserAuthority {

    VISITOR(0, "enum.user_authority.visitor"),//未登录
    USER(1, "enum.user_authority.user"),
    JUNIOR_EDITOR(2, "enum.user_authority.junior_editor"),
    SENIOR_EDITOR(3, "enum.user_authority.senior_editor"),
    ADMIN(100,"enum.user_authority.admin");

    @EnumValue
    private final Integer value;
    private final String labelKey;

    public static UserAuthority get(int value) {
        for (UserAuthority type : UserAuthority.values()) {
            if(type.value == value)
                return type;
        }
        return VISITOR;
    }

    public static boolean isJunior(User user) {
        if(user == null) return false;
        return user.getType().value >= JUNIOR_EDITOR.value;
    }

    public static boolean isSenior(User user) {
        if(user == null) return false;
        return user.getType().value >= SENIOR_EDITOR.value;
    }

    public static boolean isAdmin(User user) {
        if(user == null) return false;
        return user.getType().value.intValue() == ADMIN.value;
    }

    public static boolean isUser(User user) {
        if(user == null) return false;
        return user.getType().value > VISITOR.value;
    }

}
