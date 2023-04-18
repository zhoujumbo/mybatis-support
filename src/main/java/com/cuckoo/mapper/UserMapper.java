package com.cuckoo.mapper;

import com.cuckoo.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    //public void save(User user); //SqlSession.insert()

    public void save(User user);

    public List<User> queryAllUsersByPage();//SqlSesson.select()

    public User queryUserById(@Param("id") Integer id);

    public void update(User user);

}
