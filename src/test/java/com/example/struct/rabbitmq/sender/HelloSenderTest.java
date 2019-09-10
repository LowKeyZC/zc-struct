package com.example.struct.rabbitmq.sender;

import com.alibaba.fastjson.JSON;
import com.example.struct.common.MqDto;
import com.example.struct.util.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HelloSenderTest {

    @Resource
    private RedisUtil redisUtil;
    @Autowired
    private HelloSender helloSender;

    @Test
    public void test() {
        redisUtil.set("aaa","123");
        System.out.println(redisUtil.get("aaa"));
    }
}