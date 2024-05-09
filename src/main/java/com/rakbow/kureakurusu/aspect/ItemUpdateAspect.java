package com.rakbow.kureakurusu.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.BufferedReader;

/**
 * @author Rakbow
 * @since 2024/5/9 17:52
 */
@Aspect
@Component
public class ItemUpdateAspect {

    @Before("@annotation(com.rakbow.kureakurusu.annotation.ItemUpdate)")
    @SneakyThrows
    public void modifyParam() {
        // 获取HttpServletRequest对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();

        // 包装HttpServletRequest
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);

        // 获取请求的JSON字符串
        String jsonRequest = new String(wrappedRequest.getContentAsByteArray());
        System.out.println(STR."Request JSON: \{jsonRequest}");
    }

}
