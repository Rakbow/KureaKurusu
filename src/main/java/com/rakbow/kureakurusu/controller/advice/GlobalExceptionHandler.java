package com.rakbow.kureakurusu.controller.advice;

import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.toolkit.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Rakbow
 * @since 2022-09-12 17:31
 */
@Slf4j
@ControllerAdvice(annotations = Controller.class)
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler({Exception.class})
    public ApiResult exceptionHandler(Exception e) {
        String msg = StringUtil.isNotBlank(e.getMessage()) ? e.getMessage() : e.getCause().getMessage();
        log.error(msg, e);
        return new ApiResult().fail(msg);
    }

}
