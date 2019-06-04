package com.example.struct.util;

import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.Properties;

/**
 * 配置文件集成
 */
public class PropertyUtils {
    private static String propUrl = "prop.properties";
    private static Properties prop = new Properties();

    public static String getString(String name) {
        return prop.getProperty(name);
    }

    static {
        try {
            InputStream in = (new ClassPathResource(propUrl)).getInputStream();
            prop.load(in);
            in.close();
        } catch (Exception var1) {
            var1.printStackTrace();
        }

    }
}
