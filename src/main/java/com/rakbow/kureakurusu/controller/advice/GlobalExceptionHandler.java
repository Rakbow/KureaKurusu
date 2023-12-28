package com.rakbow.kureakurusu.controller.advice;

import com.alibaba.fastjson2.JSON;
import com.rakbow.kureakurusu.data.ApiResult;
import com.rakbow.kureakurusu.util.I18nHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2022-09-12 17:31
 * @Description: 记录异常到日志中
 */
@ControllerAdvice(annotations = Controller.class)
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({Exception.class})
    public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.error(I18nHelper.getMessage("server.exception") + e.getMessage());
        for (StackTraceElement element : e.getStackTrace()) {
            logger.error(element.toString());
        }
        String xRequestedWith = request.getHeader("x-requested-with");
        //为异步请求
        if ("XMLHttpRequest".equals(xRequestedWith)) {
            response.setContentType("application/plain;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(JSON.toJSONString(new ApiResult(e)));
        //为普通请求
        } else {
            response.sendRedirect(request.getContextPath() + "/error");
        }
    }

    @ExceptionHandler({Exception.class})
    public ApiResult handleException(Exception e) {
        return new ApiResult(e);
    }

}
