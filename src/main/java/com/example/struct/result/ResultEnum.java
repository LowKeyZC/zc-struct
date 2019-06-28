package com.example.struct.result;

public enum ResultEnum {

    /**
     * 成功
     */
    SUCCESS("200", "成功"),

    /**
     * 系统异常
     */
    ERROR("999999", "系统异常"),

    /**
     * 系统繁忙，请稍后
     */
    BUSY("999998", "系统繁忙"),

    /**
     * 无此用户
     */
    NOUSER("1001", "无此用户"),

    /**
     * 密码错误
     */
    WRONGPWD("1002", "密码错误"),

    /**
     * 余额不足
     */
    NOMONEY("1003", "余额不足"),

    /**
     * 未找到图书
     */
    NOBOOK("1004", "未找到图书"),

    ;

    private String code;

    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    ResultEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
