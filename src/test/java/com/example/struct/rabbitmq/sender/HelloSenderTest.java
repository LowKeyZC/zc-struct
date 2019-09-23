package com.example.struct.rabbitmq.sender;

import com.alibaba.fastjson.JSON;
import com.example.struct.common.MqDto;
import com.example.struct.domain.User;
import com.example.struct.service.UserService;
import com.example.struct.util.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HelloSenderTest {

    @Resource
    private RedisUtil redisUtil;
    @Autowired
    private HelloSender helloSender;
    @Resource
    private UserService userService;

    @Test
    public void test() {
        redisUtil.set("aaa","123");
        System.out.println(redisUtil.get("aaa"));
    }

    @Test
    public void test2() {
        User user1 = new User();
        user1.setId("111");
        user1.setName("改");
        User user2 = new User();
        user2.setId("222");
        user2.setName("天");
        List<User> userList = new ArrayList<>(2);
        userList.add(user1);
        userList.add(user2);
        userService.updateBatchName(userList);
    }
}