package com.cuckoo.druidpaser;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alibaba.druid.sql.repository.SchemaRepository;
import com.alibaba.druid.util.JdbcConstants;
import com.cuckoo.entity.ReportTemplateField;
import com.cuckoo.entity.User;
import com.cuckoo.mapper.GeneralJournalMapper;
import com.cuckoo.mapper.ReportTemplateFieldMapper;
import com.cuckoo.mapper.UserMapper;
import com.cuckoo.util.MyBatisUtil;
import com.cuckoo.util.ReportAdapterUtils;
import com.cuckoo.util.ReportBooster;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class TestDruidParser1 {



    /**
     * 用于测试：动态表结构查询
     */
    @Test
    public void testQuery() {
        MyBatisUtil.run((session)->{

            UserMapper mapper = session.getMapper(UserMapper.class);

            User user = mapper.queryUserById(1);

            System.out.println(user.toString());

            session.commit();
        });
    }

    @Test
    public void testSchemaRepository() {
        MyBatisUtil.run((session)->{

            final DbType dbType = JdbcConstants.MYSQL;
            SchemaRepository repository = new SchemaRepository(dbType);
//            repository.console("");

            MySqlCreateTableStatement createTableStmt = (MySqlCreateTableStatement) repository.findTable("user").getStatement();

            System.out.println(createTableStmt.getTableElementList().size());

            session.commit();


        });
    }

    /**
     * 用于测试：动态表结构查询  增强器
     */
    @Test
    public void testQueryBooter()  {
        MyBatisUtil.run((session)->{

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
        MyBatisUtil.run((session)->{

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
        MyBatisUtil.run((session)->{

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
        MyBatisUtil.run((session)->{

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
        MyBatisUtil.run((session)->{

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
//        MyBatisUtil.run((session)->{
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
        MyBatisUtil.run((session)->{

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
        MyBatisUtil.run((session)->{

            GeneralJournalMapper mapper = session.getMapper(GeneralJournalMapper.class);
            String tableName = "general_journal";
            int r = mapper.dropTable(tableName);
            System.out.println("Drop 表:" + tableName +",结果=" + (r > 0));
            session.commit();


        });
    }
}

















