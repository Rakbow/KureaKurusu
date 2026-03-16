package com.rakbow.kureakurusu.aspect;

import com.rakbow.kureakurusu.exception.UnauthorizedException;
import com.rakbow.kureakurusu.interceptor.UserContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author Rakbow
 * @since 2026/3/17 0:48
 */
@Order(1)
@Slf4j
@Component
@Aspect
public class LoginRequiredAspect {

    @Pointcut("@annotation(com.rakbow.kureakurusu.annotation.LoginRequired)")
    private void pointcut() {
    }

    @Before("pointcut()")
    public void checkLogin() {
        if(!UserContextHolder.isLogin()) throw new UnauthorizedException("auth.no_login");
    }

}
