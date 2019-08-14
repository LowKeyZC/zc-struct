package com.example.struct.controller;

import com.example.struct.rabbitmq.sender.HelloSender;
import com.example.struct.service.UserService;
import com.example.struct.util.MailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

@RestController
@RequestMapping(value = "test")
public class TestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @Resource
    private HelloSender mqsender;
    @Resource
    private UserService userService;
    @Resource
    private MailUtil mailUtil;

    @RequestMapping(value = "test01")
    public String test01(String params) throws InterruptedException {
        System.out.println(new Date());
        Thread.sleep(10000);
        System.out.println(new Date());
        return "success";
    }

}
