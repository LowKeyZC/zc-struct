package com.example.struct.result;

public class ZcResult {

    private String code;

    private String msg;

    private Object data;

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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ZcResult() {}

    public static ZcResult result(ResultEnum resultEnum) {
        ZcResult result = new ZcResult();
        result.setCode(resultEnum.getCode());
        result.setMsg(resultEnum.getMsg());
        return result;
    }

    public static ZcResult success() {
        ZcResult result = new ZcResult();
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMsg(ResultEnum.SUCCESS.getMsg());
        return result;
    }

    public static ZcResult success(Object data) {
        ZcResult result = success();
        result.setData(data);
        return result;
    }

    public static ZcResult busy() {
        return result(ResultEnum.BUSY);
    }


}
