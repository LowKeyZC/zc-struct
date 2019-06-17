package com.example.struct.controller;

import com.alibaba.fastjson.JSON;
import com.example.struct.domain.User;
import com.example.struct.rabbitmq.sender.HelloSender;
import com.example.struct.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "test")
public class TestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private HelloSender mqsender;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "test01")
    public String test01() {
        LOGGER.info("TEST");
        User user = userService.selectById("123456");
        System.out.println(JSON.toJSONString(user));
        return "success";
    }

    @RequestMapping(value = "test02")
    public String test02() {
        mqsender.send("hello");
        return "success";
    }

}
