package com.example.struct.enums;

/**
 * redis 业务枚举
 */
public enum RedisDomainEnum {
    /**
     * 默认
     */
    MYDOMAIN("myDomain"),
    /**
     * 其他
     */
    OTHER("other");

    private String name;

    public String getName(){
        return name;
    }

    private RedisDomainEnum(String name){
        this.name=name;
    }
}
