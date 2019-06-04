package com.example.struct.mapper;

import com.example.struct.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    User selectById(String id);

    void insertUser(User user);
}
