package com.example.struct.domain;

import java.sql.Date;

public class User {
    private String id;
    private String name;
    private Date birth;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public static void main(String[] args) {
        String url = "http://ipsimg-huabei2.speiyou.cn/010/video/other/20190417/f1096862154a4994a7ae2c82033e7759/40288b15685ae2b301685b4ec4ad0aba/4121372b-1341-46f7-9c30-30361bf66292.jpg";
        System.out.println(164*25*3);
    }
}
