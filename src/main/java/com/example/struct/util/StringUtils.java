package com.example.struct.util;

import java.util.UUID;

public class StringUtils {

    public static String getRandomId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
