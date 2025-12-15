package com.rakbow.kureakurusu.aspect;

import com.rakbow.kureakurusu.data.vo.EntityMiniVO;
import com.rakbow.kureakurusu.interceptor.TokenInterceptor;
import com.rakbow.kureakurusu.toolkit.CommonUtil;
import com.rakbow.kureakurusu.toolkit.HttpUtil;
import com.rakbow.kureakurusu.toolkit.StringUtil;
import com.rakbow.kureakurusu.toolkit.VisitUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author Rakbow
 * @since 2023-02-21 22:23
 */
@Component
@Aspect
@RequiredArgsConstructor
public class UniqueVisitorAspect {

    @Value("${server.servlet.context-path}")
    private String contextPath;

    private final VisitUtil visitUtil;


    @Pointcut("@annotation(com.rakbow.kureakurusu.annotation.UniqueVisitor)")
    private void pointcut() {
    }

    @Before("pointcut()")
    public void before() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) requestAttributes).getResponse();

        //if visit token empty, generate it and set in cookie
        String visitToken = TokenInterceptor.getVisitToken();
        if (StringUtil.isEmpty(visitToken)) {
            visitToken = CommonUtil.generateUUID(0);
            Cookie cookie = new Cookie("visit_token", visitToken);
            cookie.setPath(contextPath);
            assert response != null;
            response.addCookie(cookie);
        }

        //get entity info
        String uri = request.getRequestURI();
        EntityMiniVO e = HttpUtil.getEntityInfoByRequestURI(uri);
        // increase visit count
        visitUtil.increase(e.getType(), e.getId(), visitToken);
    }

}
