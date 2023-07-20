package com.rakbow.kureakurusu.data.emun.system;

import com.rakbow.kureakurusu.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-02-10 21:01
 * @Description:
 */
@AllArgsConstructor
public enum UserAuthority {


    VISITOR(0, "游客", "Visitor"),//未登录
    USER(1, "普通用户", "User"),
    JUNIOR_EDITOR(2, "初级管理员", "JuniorEditor"),
    SENIOR_EDITOR(3, "高级管理员", "SeniorEditor"),
    ADMIN(4,"超级管理员", "Admin");

    @Getter
    private final int level;
    @Getter
    private final String nameZh;
    @Getter
    private final String nameEn;

    public static boolean isJunior(User user) {
        return getUserOperationAuthority(user) >= JUNIOR_EDITOR.level;
    }

    public static boolean isSenior(User user) {
        return getUserOperationAuthority(user) >= SENIOR_EDITOR.level;
    }

    public static boolean isAdmin(User user) {
        return getUserOperationAuthority(user) == ADMIN.level;
    }

    public static boolean isUser(User user) {
        return getUserOperationAuthority(user) > VISITOR.level;
    }

    public static int getUserOperationAuthority(User user) {
        if(user != null) {
            switch (user.getType()) {
                case 0:
                    return 4;
                case 1:
                    return 1;
                case 2:
                    return 2;
                case 3:
                    return 3;
            }
        }
        return 0;
    }

}
