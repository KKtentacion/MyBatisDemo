package com.Kkktentacion;

import com.Kkktentacion.mapper.UserMapper;
import com.Kkktentacion.pojo.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MyBatisDemo {
    public static void main(String[] args) throws IOException {
        String resource = "MyBatis.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession sqlSession = sqlSessionFactory.openSession();

//        List<User> users=sqlSession.selectList("UserMapper.selectAll");

        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        List<User> users=userMapper.selectAll();

        System.out.println(users);

        sqlSession.close();
    }
}