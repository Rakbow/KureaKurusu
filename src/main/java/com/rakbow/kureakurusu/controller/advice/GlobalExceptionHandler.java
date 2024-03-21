package com.rakbow.kureakurusu.controller.advice;

import com.rakbow.kureakurusu.data.common.ApiResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Rakbow
 * @since 2022-09-12 17:31
 */
@ControllerAdvice(annotations = Controller.class)
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseBody
    @ExceptionHandler({Exception.class})
    public ApiResult exceptionHandler(Exception e) {
        String msg = StringUtils.isNotBlank(e.getMessage()) ? e.getMessage() : e.getCause().getMessage();
        log.error(msg, e);
        return new ApiResult().fail(msg);
    }

}
