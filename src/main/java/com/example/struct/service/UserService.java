package com.example.struct.service;

import com.alibaba.fastjson.JSON;
import com.example.struct.annotation.Master;
import com.example.struct.domain.User;
import com.example.struct.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
}
