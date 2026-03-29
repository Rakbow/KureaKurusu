package com.rakbow.kureakurusu.aspect;

import com.rakbow.kureakurusu.annotation.Permission;
import com.rakbow.kureakurusu.data.auth.LoginUser;
import com.rakbow.kureakurusu.data.constant.PermissionConstant;
import com.rakbow.kureakurusu.data.enums.PermissionLogical;
import com.rakbow.kureakurusu.exception.ErrorFactory;
import com.rakbow.kureakurusu.interceptor.UserContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

/**
 * @author Rakbow
 * @since 2026/3/14 13:13
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class PermissionAspect {

    @Pointcut("@annotation(com.rakbow.kureakurusu.annotation.Permission)")
    private void pointcut() {
    }

    @Before("pointcut()")
    public void checkPermission(JoinPoint j) {
        MethodSignature signature = (MethodSignature) j.getSignature();
        Method targetMethod = signature.getMethod();
        Permission permission = targetMethod.getAnnotation(Permission.class);
        if (permission == null) return;

        LoginUser user = UserContextHolder.getCurrentUser();

        if(Objects.isNull(user)) throw ErrorFactory.unauthorized();

        Set<String> permissions = user.getPermissions();

        String[] perms = permission.value();
        PermissionLogical logical = permission.logical();

        //admin
        if(permissions.contains(PermissionConstant.ADMIN)) return;

        if (logical == PermissionLogical.AND) {
            for (String perm : perms) {
                if (permissions.contains(perm)) continue;

                throw ErrorFactory.noPermission();
            }
        } else {
            if (Arrays.stream(perms).noneMatch(permissions::contains))
                throw ErrorFactory.noPermission();
        }

    }

}
