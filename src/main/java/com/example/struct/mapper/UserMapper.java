package com.example.struct.mapper;

import com.example.struct.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    User selectById(String id);

    List<User> selectNowBirthDay();

    int insertUser(User user);

    int insertBatch(List<User> users);

    User selectByIdAndName(String id, String name);

    int updateUserById(User user);

    int updateBatchName(List<User> users);
}
