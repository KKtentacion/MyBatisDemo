package com.Kkktentacion.mapper;

import com.Kkktentacion.pojo.User;

import java.util.List;

public interface UserMapper {
    List<User> selectAll();
    User selectById(int id);
}
