package com.example.struct.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.UUID;

public class RandomUtil {

    public static String getUuId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String getRandom50() {
        return RandomStringUtils.random(50,true,true);
    }

    public static String getMd5Str(String string) {
        return DigestUtils.md5Hex(string);
    }
}
