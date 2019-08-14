package com.example.struct.common;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * rabbitmq传递数据对象
 */
public class MqDto {

    private Integer msgType;
    private Object data;

    public MqDto(Integer msgType, Object data) {
        this.msgType = msgType;
        this.data = data;
    }

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
