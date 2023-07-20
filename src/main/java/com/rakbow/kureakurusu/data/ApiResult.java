package com.rakbow.kureakurusu.data;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2022-09-04 3:45
 * @Description:
 */
public class ApiResult {

    public int code;//操作代码
    public int state;//操作状态 0-失败 1-成功
    public Object data;//响应数据
    public String message;//错误信息

    public ApiResult() {
        this.state = 1;
        this.message = "";
    }

    public ApiResult(int state, String message) {
        this.state = state;
        this.message = message;
    }

    public void setErrorMessage(String error) {
        this.state = 0;
        this.message = error;
    }

    public void setErrorMessage(Exception e) {
        this.state = 0;
        this.message = e.getMessage();
    }

}
