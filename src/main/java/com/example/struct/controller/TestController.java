package com.example.struct.controller;

import com.alibaba.fastjson.JSON;
import com.example.struct.common.Constant;
import com.example.struct.common.MqDto;
import com.example.struct.domain.User;
import com.example.struct.rabbitmq.sender.HelloSender;
import com.example.struct.service.UserService;
import com.example.struct.util.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "test")
public class TestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @Resource
    private HelloSender mqsender;
    @Resource
    private UserService userService;

    @RequestMapping(value = "test01")
    public String test01(String params) {
        User user = userService.selectById(params);
        System.out.println(JSON.toJSONString(user));
        return "success";
    }

    @RequestMapping(value = "test02")
    public String test02() {
        User user = new User();
        user.setId("123");
        user.setName("abc");

        MqDto mqDto = new MqDto(Constant.MQ_TYPE_ONE, user);
        mqsender.send(mqDto);
        return "success";
    }

}
