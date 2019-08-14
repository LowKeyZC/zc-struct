package com.example.struct.service;

import com.alibaba.fastjson.JSON;
import com.example.struct.annotation.Master;
import com.example.struct.domain.User;
import com.example.struct.mapper.UserMapper;
import com.example.struct.util.RandomUtil;
import com.example.struct.util.RedisClientTool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public void insertUser(User user) {
        userMapper.insertUser(user);
    }

    public int updateUserById(User user) {
        return userMapper.updateUserById(user);
    }

//    @Transactional
    public void test01(String id) throws InterruptedException {
        for (int j = 0; j < 10; j++) {
            List<User> users = new ArrayList<>();
            for (int i = 0; i < 10000; i++) {
                User user = new User();
                user.setId(RandomUtil.getUuId());
                user.setName("小小");
                user.setBirthday(new Date());
                user.setAge(12);
                users.add(user);
            }
            int re = userMapper.insertBatch(users);
            if (re == users.size()) {
                System.out.println("插入成功");
                continue;
            }
            System.out.println("插入失败");
        }
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
