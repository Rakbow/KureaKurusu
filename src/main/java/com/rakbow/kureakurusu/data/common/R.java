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
public class R {

    /**
     * Http Status Code
     */
    private int state;//操作状态 0-失败 1-成功
    private Object data;//响应数据
    private long total;//数据总数
    private String message;//错误信息

    private static final int SUCCESS_STATE = 1;
    private static final int FAIL_STATE = 0;

    public R() {
        this.state = SUCCESS_STATE;
        this.message = "";
    }

    public R(int state, String message) {
        this.state = state;
        this.message = message;
    }

    public R(Exception e) {
        this.state = FAIL_STATE;
        this.message = e.getMessage();
    }

    public R(Object data) {
        this.data = data;
    }

    public static R ok() {
        return new R();
    }

    public static R ok(Object data) {
        R r =  new R();
        r.data = data;
        return r;
    }

    public static R ok(String messageKey) {
        R r =  new R();
        r.message = I18nHelper.getMessage(messageKey);
        return r;
    }

    public R ok(Object data, String messageKey) {
        this.data = data;
        this.message = I18nHelper.getMessage(messageKey);
        return this;
    }

    public static R fail(String error) {
        return new R(FAIL_STATE, error);
    }

    public R fail(BindingResult errors) {
        this.state = FAIL_STATE;
        this.message = errors.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("\n"));
        return this;
    }

}
