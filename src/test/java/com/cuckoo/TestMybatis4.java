package com.cuckoo;

import com.cuckoo.mapper.UserMapper;
import com.cuckoo.entity.User;
import com.cuckoo.util.Page;
import com.cuckoo.util.ThreadLocalUtils;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.List;

public class TestMybatis4 {
    /**
     * 用于测试:Plugins的基本使用
     */
    @Test
    public void test1() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = sqlSessionFactory.openSession();

        UserMapper userMapper = session.getMapper(UserMapper.class);

        User user = userMapper.queryUserById(4);
        System.out.println("user = " + user);

        User newUser = new User(4, "xiaohuahua");
        userMapper.update(newUser);

        session.commit();
    }

    /**
     * 用于测试:第一个Mybatis程序的测试
     */
    @Test
    public void test2() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = sqlFactory.openSession();

        UserMapper userMapper = session.getMapper(UserMapper.class);

        try {
            User user = userMapper.queryUserById(4);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 用于测试:PageHelper
     */
    @Test
    public void test4() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = sqlSessionFactory.openSession();

        UserMapper userMapper = session.getMapper(UserMapper.class);

        //Controller给我们的
        //Page page = new Page(2);


        //通过Filter设置Page
        Page page = new Page(1);
        ThreadLocalUtils.set(page);


        List<User> users = userMapper.queryAllUsersByPage();

        for (User user : users) {
            System.out.println("user = " + user);
        }


        User user1 = userMapper.queryUserById(4);

        User user = new User();
        user.setId(4);
        user.setName("xiaowb");
        userMapper.update(user);

        session.commit();
    }

    /**
     * 用于测试:
     */
    @Test
    public void testSql() {
        String sql = "select id,name from t_user where name = ? and id = ?";
        String countSql = "select count(*) " + sql.substring(sql.indexOf("from"));
        System.out.println("countSql = " + countSql);
    }


    /**
     * 用于测试:
     */
    @Test
    public void testThreadLocal() {
        ThreadLocal<String> tl = new ThreadLocal<>();
        //获取 当前线程对象 并把suns 存储在了当前线程对象中的Map key=tl value='suns'
        tl.set("suns");


        Thread thread = Thread.currentThread();

        System.out.println("============================");
    }

    /**
     * 用于测试:jSqlparser
     */
    @Test
    public void testSQLParser() throws JSQLParserException {
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        Select select = (Select) parserManager.parse(new StringReader("select id,name from t_user where name = 'suns' "));

        PlainSelect selectBody = (PlainSelect) select.getSelectBody();

        //FromItem table = selectBody.getFromItem();
        //System.out.println("table = " + table);

     /*   Expression where = selectBody.getWhere();
        System.out.println("where = " + where);*/

       /* List<SelectItem> selectItems = selectBody.getSelectItems();
        for (SelectItem selectItem : selectItems) {
            System.out.println("selectItem = " + selectItem);
        }*/
    }

    @Test
    public void testSQLParser1() throws JSQLParserException {
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        Update update = (Update) parserManager.parse(new StringReader("update t_user set name='suns',password='12345' where id=1 "));

        /*Table table = update.getTable();
        System.out.println("table = " + table);*/

        List<Column> columns = update.getColumns();
        for (Column column : columns) {
            System.out.println(column);
        }

        List<Expression> expressions = update.getExpressions();
        for (Expression expression : expressions) {
            System.out.println(expression);
        }
    }

    /**
     * 用于测试:乐观锁相关的开发
     */
    @Test
    public void testLock() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);


        User user = new User();
        user.setName("xiaoheihei");

        userMapper.save(user);

        sqlSession.commit();

    }
    
    /**
     *  用于测试:乐观锁的更新操作
     */
    @Test
    public void testLock2() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        //从数据库中查询出来的
        User user = new User();
        user.setId(4);
        user.setName("xiaoheihei3");
        user.setVersion(0);

        userMapper.update(user);

        sqlSession.commit();


    }
}

















