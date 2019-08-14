package com.example.struct.common;

public class Constant {
    /* rabbitmq queue */
    public static final String LOCAL_COMMON_QUEUE = "RABBITMQ_LOCAL_COMMON_QUEUE";  //本地发送mq
    public static final String THIRD_MODULE_QUEUE = "RABBITMQ_THIRD_MODULE_QUEUE";  //发送其他MQ

    /* rabbitmq type */
    public static final Integer MQ_TYPE_ONE = 1;
    public static final Integer MQ_TYPE_TWO = 2;

    /* dynamic task */
    public static final String DYNAMIC_TASK1 = "DYNAMIC_TASK1";
    public static final String DYNAMIC_TASK2 = "DYNAMIC_TASK2";
}
