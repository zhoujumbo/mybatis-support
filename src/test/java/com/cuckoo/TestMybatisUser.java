package com.cuckoo;

import com.cuckoo.entity.ReportTemplateField;
import com.cuckoo.entity.User;
import com.cuckoo.mapper.GeneralJournalMapper;
import com.cuckoo.mapper.ReportTemplateFieldMapper;
import com.cuckoo.util.ReportAdapterUtils;
import com.cuckoo.util.ReportBooster;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class TestMybatisUser {



    public void before(Consumer<SqlSession> bus) {
        SqlSession session = null;
        try (InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml")){
//        try (InputStream inputStream = Resources.getResourceAsStream("mybatis-config2.xml")){
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            session = sqlSessionFactory.openSession();
            bus.accept(session);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(session != null){
                session.close();
            }
        }
    }


    /**
     * 用于测试：动态表结构查询
     */
    @Test
    public void testUserQuery()  {
        before((session)->{

        List<User> users = session.selectList("com.cuckoo.dao.UserDAO.queryAllUsers");
        for (User user : users) {
            System.out.println("user = " + user);
        }


        });
    }




    /**
     * 用于测试：动态表结构查询
     */
    @Test
    public void testQuery()  {
        before((session)->{

            GeneralJournalMapper mapper = session.getMapper(GeneralJournalMapper.class);

            //        Map<String, Object> map = mapper.queryById(1);

            List<Map<String, Object>> mapList = mapper.query();

            System.out.println(mapList.toString());

            session.commit();


        });
    }


    /**
     * 用于测试：动态表结构查询  增强器
     */
    @Test
    public void testQueryBooter()  {
        before((session)->{

            // 解析字段
            ReportTemplateFieldMapper fieldMapper = session.getMapper(ReportTemplateFieldMapper.class);
            List<ReportTemplateField> fieldList = fieldMapper.queryByTemplateCode("11100001");
            List<String> fieldItems = fieldList.stream()
                    .filter(Objects::nonNull)
                    .filter(var-> Optional.ofNullable(var.getChoose()).orElse(false))
                    .map(ReportTemplateField::getFieldCode)
                    .collect(Collectors.toList());
//            String fieldString =  fieldList.stream()
//                    .filter(Objects::nonNull)
//                    .filter(var-> Optional.ofNullable(var.getChoose()).orElse(false))
//                    .map(ReportTemplateField::getFieldCode)
//                    .collect(Collectors.joining(","));
//
//            System.out.println(fieldString);


            ReportBooster booster = new ReportBooster();
            booster.setSqlType("select");
            booster.setSchemaName("enterprise_financial");
            booster.setTableName("general_journal_v2022062201");
            booster.setFieldItems(fieldItems);
            ReportAdapterUtils.set(booster);

            GeneralJournalMapper mapper = session.getMapper(GeneralJournalMapper.class);

            //        Map<String, Object> map = mapper.queryById(1);

            List<Map<String, Object>> mapList = mapper.query();

            System.out.println(mapList.toString());

            session.commit();


        });
    }

    @Test
    public void existTable() {
        before((session)->{

            GeneralJournalMapper mapper = session.getMapper(GeneralJournalMapper.class);
            String tableName = "general_journal";
            int r = mapper.existTable(tableName);
            System.out.println("表【" + tableName +"】是否存在 = " + (r > 0));
            session.commit();


        });
    }


// dropTable(String tableName);
    @Test
    public void dropTable() {
        before((session)->{

            GeneralJournalMapper mapper = session.getMapper(GeneralJournalMapper.class);
            String tableName = "general_journal";
            int r = mapper.dropTable(tableName);
            System.out.println("Drop 表:" + tableName +",结果=" + (r > 0));
            session.commit();


        });
    }

// truncate(String tableName);
    @Test
    public void truncate() {
        before((session)->{

            GeneralJournalMapper mapper = session.getMapper(GeneralJournalMapper.class);
            String tableName = "general_journal";
            mapper.truncateTable(tableName);
            System.out.println("清空 表:" + tableName);
            session.commit();
        });
    }

// copyTable(String targetTableName, String targetFields, String sourceTableName, String sourceFields, String sourceCondition);
    @Test
    public void copyTable() {
        before((session)->{

            // 解析字段
            ReportTemplateFieldMapper fieldMapper = session.getMapper(ReportTemplateFieldMapper.class);
            List<ReportTemplateField> fieldList = fieldMapper.queryByTemplateCode("11100001");
            String fieldString =  fieldList.stream()
                    .filter(Objects::nonNull)
                    .filter(var-> Optional.ofNullable(var.getChoose()).orElse(false))
                    .map(ReportTemplateField::getFieldCode)
                    .collect(Collectors.joining(","));

            System.out.println(fieldString);


            GeneralJournalMapper mapper = session.getMapper(GeneralJournalMapper.class);



            String targetTableName = "general_journal_v2022062201";
            String targetFields = "id," + fieldString;
            String sourceTableName = "general_journal";
            String sourceFields = "id," + fieldString;
            String sourceCondition = "";
            mapper.copyTableDdl(targetTableName, sourceTableName);
            mapper.copyAllTableData(targetTableName, sourceTableName);
//            int r = mapper.copyTable(targetTableName, targetFields, sourceTableName, sourceFields, sourceCondition);
//            System.out.println("Copy 表:" + sourceTableName +",结果=" + (r > 0));
            session.commit();


        });
    }

// copyTableByTemplate(String targetTableName, List<ReportTemplateField> targetFields, String sourceTableName, List<ReportTemplateField> sourceFields, String sourceCondition);
//    @Test
//    public void copyTableByTemplate() {
//        before((session)->{
//
//            GeneralJournalMapper mapper = session.getMapper(GeneralJournalMapper.class);
//            String tableName = "general_journal";
//            int r = mapper.dropTable(tableName);
//            System.out.println("Drop 表:" + tableName +",结果=" + (r > 0));
//            session.commit();
//
//
//        });
//    }

// createCustomTable(String tableName, List<ReportTemplateField> customTables);
    @Test
    public void createCustomTable() {
        before((session)->{

            GeneralJournalMapper mapper = session.getMapper(GeneralJournalMapper.class);
            String tableName = "general_journal";
            int r = mapper.dropTable(tableName);
            System.out.println("Drop 表:" + tableName +",结果=" + (r > 0));
            session.commit();


        });
    }

// cusInsert(String tableName, String tableInsertFields, String tableInsertFieldValues);
    @Test
    public void cusInsert() {
        before((session)->{

            GeneralJournalMapper mapper = session.getMapper(GeneralJournalMapper.class);
            String tableName = "general_journal";
            int r = mapper.dropTable(tableName);
            System.out.println("Drop 表:" + tableName +",结果=" + (r > 0));
            session.commit();


        });
    }
}

















