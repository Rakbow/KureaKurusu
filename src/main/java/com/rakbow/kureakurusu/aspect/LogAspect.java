package com.rakbow.kureakurusu.aspect;

import com.rakbow.kureakurusu.toolkit.DateHelper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author Rakbow
 * @since 2022-09-12 20:04
 */
@Slf4j
@Component
@Aspect
public class LogAspect {

    @Pointcut("execution(* com.rakbow.kureakurusu.controller.*.*(..))")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) return;
        HttpServletRequest req = attrs.getRequest();
        String ip = req.getRemoteHost();
        String now = DateHelper.nowDate();
        String target = STR."\{joinPoint.getSignature().getDeclaringTypeName()}.\{joinPoint.getSignature().getName()}";
        log.info("User {} in {} access {}", ip, now, target);
    }

}
