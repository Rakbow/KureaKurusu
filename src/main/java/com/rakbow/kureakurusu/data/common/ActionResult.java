package com.rakbow.kureakurusu.data.common;

/**
 * @author Rakbow
 * @since 2022-09-30 11:14
 */
public class ActionResult {

    public boolean state;//操作状态
    public String message;//错误信息
    public Object data;//返回数据

    public ActionResult() {
        this.state = true;
        this.message = "";
        this.data = null;
    }

    public void setErrorMessage(String error) {
        this.state = false;
        this.message = error;
        this.data = null;
    }

    public boolean success() {
        return this.state;
    }

}
