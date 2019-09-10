package com.example.struct.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class CommonUtil {
  private static Properties prop = new Properties();

  static {
    try(InputStream in = (new ClassPathResource("prop.properties")).getInputStream()) {
      prop.load(in);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 获取配置属性
   */
  public static String getProperty(String key) {
    return prop.getProperty(key);
  }

  /**
   * 多参数非空校验
   */
  public static boolean validParams(Object... args) {
    for (Object arg : args) {
      if (arg == null) {
        return false;
      }
      if (arg instanceof Collection && ((Collection<?>) arg).isEmpty()) {
        return false;
      }
      if (arg instanceof Map && ((Map<?, ?>) arg).isEmpty()) {
        return false;
      }
      if (arg instanceof CharSequence && ((CharSequence) arg).length() == 0) {
        return false;
      }
      if (arg.getClass().isArray() && Array.getLength(arg) == 0) {
        return false;
      }
    }
    return true;
  }

  /**
   * ListToMap
   */
  public static <T> Map<Object, T> listToMap(String methodName, Collection<T> list) {
    Map<Object,T> map = new HashMap<>();
    try {
      for (T t : list) {
        map.put(t.getClass().getMethod(methodName).invoke(t), t);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return map;
  }
}
