package com.example.struct.mapper;

import com.example.struct.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface UserMapper {

    User selectById(String id);

    List<User> selectNowBirthDay();

    void insertUser(User user);
}
