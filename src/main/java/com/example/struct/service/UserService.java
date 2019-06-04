package com.example.struct.service;

import com.example.struct.conf.db.DS;
import com.example.struct.domain.User;
import com.example.struct.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @DS("slaveDataSource")
    public User selectById(String id) {
        return userMapper.selectById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void insertUser(User user) {
        userMapper.insertUser(user);
    }
}
