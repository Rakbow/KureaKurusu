package com.rakbow.kureakurusu.aspect;

import com.rakbow.kureakurusu.toolkit.DateHelper;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Rakbow
 * @since 2022-09-12 20:04
 */
@Slf4j
@Component
@Aspect
public class ServiceLogAspect {

    @Pointcut("execution(* com.rakbow.kureakurusu.service.*.*(..))")
    public void pointcut() {

    }

    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String ip = request.getRemoteHost();
            String now = new SimpleDateFormat(DateHelper.DATE_TIME_FORMAT).format(new Date());
            String target = STR."\{joinPoint.getSignature().getDeclaringTypeName()}.\{joinPoint.getSignature().getName()}";
            log.info(String.format(I18nHelper.getMessage("system.service.log"), ip, now, target));
        }
    }

}
