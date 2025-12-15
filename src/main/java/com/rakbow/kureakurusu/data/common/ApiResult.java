package com.rakbow.kureakurusu.data.common;

import com.rakbow.kureakurusu.toolkit.I18nHelper;
import lombok.Data;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.stream.Collectors;

/**
 * @author Rakbow
 * @since 2022-09-04 3:45
 */
@Data
public class ApiResult {

    private String code;//操作代码
    private int state;//操作状态 0-失败 1-成功
    private Object data;//响应数据
    private long total;//数据总数
    private String message;//错误信息

    private static final int SUCCESS_STATE = 1;
    private static final String SUCCESS_CODE = "200";
    private static final int FAIL_STATE = 0;
    private static final String FAIL_CODE = "500";

    public ApiResult() {
        this.state = SUCCESS_STATE;
        this.code = SUCCESS_CODE;
        this.message = "";
    }

    public ApiResult(int state, String message) {
        this.state = state;
        this.message = message;
    }

    public ApiResult(Exception e) {
        this.state = FAIL_STATE;
        this.code = FAIL_CODE;
        this.message = e.getMessage();
    }

    public ApiResult(Object data) {
        this.data = data;
    }

    public static ApiResult ok() {
        return new ApiResult();
    }

    public static ApiResult ok(Object data) {
        ApiResult r =  new ApiResult();
        r.data = data;
        return r;
    }

    public static ApiResult ok(String messageKey) {
        ApiResult r =  new ApiResult();
        r.message = I18nHelper.getMessage(messageKey);
        return r;
    }

    public ApiResult ok(Object data, String messageKey) {
        this.data = data;
        this.code = SUCCESS_CODE;
        this.message = I18nHelper.getMessage(messageKey);
        return this;
    }

    public ApiResult fail(String error) {
        this.state = FAIL_STATE;
        this.code = FAIL_CODE;
        this.message = error;
        return this;
    }

    public ApiResult fail(BindingResult errors) {
        this.state = FAIL_STATE;
        this.message = errors.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("\n"));
        this.code = FAIL_CODE;
        return this;
    }

}
