package com.example.struct.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.poi.excel.ExcelReader;
import com.alibaba.fastjson.JSON;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
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

  public static void main(String[] args) {
    /*int zeroNum = 0;
    for (String id : TempContant.idCardNos) {
      int age = IdcardUtils.getAgeByIdCard(id);
      System.out.println("id:" + id + " 周岁:" + age);
      if (age == 0) zeroNum++;
    }
    System.out.println(TempContant.idCardNos.length + " " + zeroNum);*/
    /*Integer a = 1;
    Integer b = 2;
    a.equals(b);
    System.out.println();*/
    StringBuilder total = new StringBuilder();

    ExcelReader reader = new ExcelReader("D:\\work\\6 司机运费贷优化_进项税_其他\\云信银行卡映射表-技术用.xlsx", 0);
    List<List<Object>> contents = reader.read();
    for (int i = 1; i < contents.size(); i++) {
      List<Object> rowContent = contents.get(i);
      StringBuilder sb = new StringBuilder("INSERT INTO fuyou_bank_map(fuyouBankName,thirdBankName," +
          "thirdType,thirdCode,stdBankCode)VALUES(");
      sb.append("'").append(rowContent.get(3)).append("',");
      sb.append("'").append(rowContent.get(1)).append("',");
      sb.append(1).append(",");
      sb.append("'").append(rowContent.get(0)).append("',");
      sb.append("'").append(rowContent.get(2)).append("');\n");
      total.append(sb);
    }
    System.out.println(total.toString());
  }
}
