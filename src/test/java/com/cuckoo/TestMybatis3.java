package com.cuckoo;

import com.cuckoo.mapper.UserMapper;
import com.cuckoo.entity.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class TestMybatis3 {
    /**
     * 用于测试:一级缓存
     */
    @Test
    public void test1() throws IOException {


        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        /*UserDAO userDAO = sqlSession.getMapper(UserDAO.class);

        List<User> users = userDAO.queryAllUsers();*/

        List<User> users = sqlSession.selectList("com.cuckoo.dao.UserDAO.queryAllUsers");

        for (User user : users) {
            System.out.println("user = " + user);
        }

        System.out.println("------------------------------------------------------");

        List<User> users1 = sqlSession.selectList("com.cuckoo.dao.UserDAO.queryAllUsers");

        for (User user : users1) {
            System.out.println("user = " + user);
        }

    }

    @Test
    public void test2() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession1 = sqlSessionFactory.openSession();
        SqlSession sqlSession2 = sqlSessionFactory.openSession();
        SqlSession sqlSession3 = sqlSessionFactory.openSession();


        UserMapper userMapper1 = sqlSession1.getMapper(UserMapper.class);
        UserMapper userMapper2 = sqlSession2.getMapper(UserMapper.class);
        UserMapper userMapper3 = sqlSession3.getMapper(UserMapper.class);

       /* List<User> users1 = userDAO1.queryAllUsersByPage();//--- ms ---> cache

        for (User user : users1) {
            System.out.println("user = " + user);
        }*/

        User user1 = userMapper1.queryUserById(4); //---ms ---> cache

        sqlSession1.commit();

        //userDAO1.queryAllUsers();

        System.out.println("-----------------------------------------");

        /*List<User> users2 = userDAO2.queryAllUsersByPage();

        for (User user : users2) {
            System.out.println("user = " + user);
        }*/

        sqlSession2.commit();

        System.out.println("-----------------------------------------");
//
 /*       User user = new User(4, "xiaowb");
        userDAO3.update(user);

        sqlSession3.commit();*/

    }
    
  

}
