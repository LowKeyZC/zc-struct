package com.example.struct.aop;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAop {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Pointcut("@annotation(com.example.struct.annotation.Log)")
  public void readPointcut() {}

  @Before("readPointcut()")
  public void beforePointcut(JoinPoint joinPoint) {
    //todo your task
  }

  @After("readPointcut()")
  public void afterPointcut(JoinPoint joinPoint) {
    String className = joinPoint.getTarget().getClass().getName();
    String methodName = joinPoint.getSignature().getName();
    String[] paramsNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
    Object[] paramsValues = joinPoint.getArgs();
    StringBuilder str = new StringBuilder("接口调用记录日志：" + className + '.' + methodName + "()" +
        " 参数列表：");
    for (int i = 0; i < paramsNames.length; i++) {
      str.append(paramsNames[i]).append(" -> ").append(paramsValues[i]).append(" ");
    }
    logger.info(str.toString());
  }
}
