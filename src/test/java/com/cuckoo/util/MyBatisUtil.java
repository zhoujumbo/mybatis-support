package com.cuckoo.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

public class MyBatisUtil {

    private static SqlSessionFactory factory = null;

    // 使用static静态代码块，随着类的加载而加载，只执行一次
    static {
        try (InputStream inputStream = Resources.getResourceAsStream("mybatis/mybatis-config.xml")){
            factory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void run(Consumer<SqlSession> bus) {
        SqlSession session = null;
        try {
            session = factory.openSession();
            bus.accept(session);
        } finally {
            if(session != null){
                session.close();
            }
        }
    }

}
