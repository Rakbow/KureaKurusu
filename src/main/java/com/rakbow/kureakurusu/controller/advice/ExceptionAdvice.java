package com.rakbow.kureakurusu.controller.advice;

import com.alibaba.fastjson2.JSON;
import com.rakbow.kureakurusu.data.ApiResult;
import com.rakbow.kureakurusu.service.I18nService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import com.rakbow.kureakurusu.data.ApiInfo;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2022-09-12 17:31
 * @Description: 记录异常到日志中
 */
@ControllerAdvice(annotations = Controller.class)
public class ExceptionAdvice {

    @Resource
    private I18nService i18n;

    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler({Exception.class})
    public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.error(i18n.getMessage("server.exception") + e.getMessage());
        for (StackTraceElement element : e.getStackTrace()) {
            logger.error(element.toString());
        }

        String xRequestedWith = request.getHeader("x-requested-with");
        //为异步请求
        if ("XMLHttpRequest".equals(xRequestedWith)) {
            response.setContentType("application/plain;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(JSON.toJSONString(new ApiResult(0, e.getMessage())));
        //为普通请求
        } else {
            response.sendRedirect(request.getContextPath() + "/error");
        }
    }

}
