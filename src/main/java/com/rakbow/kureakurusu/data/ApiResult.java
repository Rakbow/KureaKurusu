package com.rakbow.kureakurusu.data;

import com.alibaba.fastjson2.JSON;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Rakbow
 * @since 2022-09-04 3:45
 */
public class ApiResult {

    public int code;//操作代码
    public int state;//操作状态 0-失败 1-成功
    public Object data;//响应数据
    public long total;//数据总数
    public String message;//错误信息

    private final int SUCCESS_CODE = 1;
    private final int FAIL_CODE = 0;

    public ApiResult() {
        this.state = SUCCESS_CODE;
        this.message = "";
    }

    public ApiResult(int state, String message) {
        this.state = state;
        this.message = message;
    }

    public ApiResult(Exception e) {
        this.state = FAIL_CODE;
        this.message = e.getMessage();
    }

    public void setErrorMessage(String error) {
        this.state = FAIL_CODE;
        this.message = error;
    }

    public void setErrorMessage(Exception e) {
        this.state = FAIL_CODE;
        this.message = e.getMessage();
    }

    public void setErrorMessage(List<FieldError> errors) {
        this.state = FAIL_CODE;
        this.message = errors.stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("\n"));
    }

    public String toJson() {
        return JSON.toJSONString(this);
    }

    public ApiResult ok(String message) {
        this.message = message;
        return this;
    }

    public ApiResult fail(Exception e) {
        this.state = FAIL_CODE;
        this.message = e.getMessage();
        return this;
    }

    public ApiResult fail(String error) {
        this.state = FAIL_CODE;
        this.message = error;
        return this;
    }

    public ApiResult fail(BindingResult errors) {
        this.state = FAIL_CODE;
        this.message = errors.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("\n"));
        return this;
    }

    public void loadDate(Object data) {
        this.data = data;
    }
}
