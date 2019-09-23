package com.example.struct.domain;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private String id;
    private String name;
    private int age;
    private Date birthday;

    public void test() {
        System.out.println("invoke method test");
    }

    public static void main(String[] args) {
        User user = new User();
        user.setName("aaa");
        user.setId("!23");
        System.out.println(user.toString());
    }
}
