package com.example.struct.rabbitmq.sender;

import com.example.struct.common.MqDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HelloSenderTest {

    @Autowired
    private HelloSender helloSender;

    /*@Test
    public void send() {
        MqDto mqDto = new MqDto();
        mqDto.setMsgType(1);
        mqDto.setData("aaa");
        helloSender.send(mqDto);
    }*/
}