package com.example.struct;

import com.example.struct.enums.RedisDomainEnum;
import com.example.struct.util.PropertyUtils;
import com.example.struct.util.RedisClientTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test {

    private static final Logger LOGGER = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) {
//        System.out.println(PropertyUtils.getString("myprop"));
//        RedisClientTool.set(RedisDomainEnum.MYDOMAIN.getName(), "aaa", "123");
//        System.out.println(RedisClientTool.get(RedisDomainEnum.MYDOMAIN.getName(),"aaa"));
        LOGGER.info("日志");
    }
}
