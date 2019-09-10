package com.example.struct.service;

import com.alibaba.fastjson.JSON;
import com.example.struct.annotation.Master;
import com.example.struct.domain.User;
import com.example.struct.mapper.UserMapper;
import com.example.struct.util.RandomUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    public User selectById(String id) {
        return userMapper.selectById(id);
    }

    public List<User> selectByBirthDay() {
        return userMapper.selectNowBirthDay();
    }

    public int insertUser(User user) {
        return userMapper.insertUser(user);
    }

    public int updateUserById(User user) {
        return userMapper.updateUserById(user);
    }

    @Transactional
    public void test01(String id) throws InterruptedException {
        User user1 = new User();
        user1.setId(RandomUtil.getUuId());
        user1.setName("小小");
        User user2 = new User();
        user2.setId(RandomUtil.getUuId());
        user2.setName("小小");
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(() -> {
            try {
                int a = insertUser(user1);
                System.out.println("user1 a = " + a);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        executorService.submit(() -> {
            try {
                int a = insertUser(user2);
                System.out.println("user2 a = " + a);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }




    @Transactional
    public void test02(String id) {
        User user = userMapper.selectById(id);
        user.setName("333");
        System.out.println("test02查询成功");
        int a = userMapper.updateUserById(user);
        if (a > 0) {
            System.out.println("修改成功");
            return;
        }
        System.out.println("修改失败");
    }

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        Class<User> userClass = User.class;
        Method method = userClass.getMethod("test");
        method.invoke(userClass.newInstance());
    }
}
