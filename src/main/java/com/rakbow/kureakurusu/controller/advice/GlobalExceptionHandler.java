package com.rakbow.kureakurusu.controller.advice;

import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.exception.EntityNullException;
import com.rakbow.kureakurusu.exception.PermissionException;
import com.rakbow.kureakurusu.exception.UnauthorizedException;
import com.rakbow.kureakurusu.toolkit.StringUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    public ApiResult exceptionHandler(Exception e, HttpServletResponse response) {
         String msg = StringUtil.isNotBlank(e.getMessage()) ? e.getMessage() : e.getCause().getMessage();
        switch (e) {
            case EntityNullException _ -> response.setStatus(HttpStatus.NOT_FOUND.value());
            case UnauthorizedException _ -> response.setStatus(HttpStatus.UNAUTHORIZED.value());
            case PermissionException _ -> response.setStatus(HttpStatus.FORBIDDEN.value());
            default -> response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        log.error(msg);
        return new ApiResult().fail(msg);
    }

}
