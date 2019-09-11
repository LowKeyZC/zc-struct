package com.example.struct.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.struct.annotation.Log;
import com.example.struct.domain.User;
import com.example.struct.enums.DBTypeEnum;
import com.example.struct.rabbitmq.sender.HelloSender;
import com.example.struct.result.ZcResult;
import com.example.struct.service.UserService;
import com.example.struct.util.HttpUtilHelper;
import com.example.struct.util.MailUtil;
import com.example.struct.util.ResultStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

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
  @Value("${server.port}")
  private Integer serverPort;

  @Log
  @RequestMapping(value = "test01")
  public ZcResult test01(String params, String params2) throws InterruptedException {
    System.out.println("当前端口：" + serverPort);
    return ZcResult.success("当前端口：" + serverPort);
  }
}
