package com.cuckoo.mapper;


import com.cuckoo.entity.ReportTemplateField;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface GeneralJournalMapper {

    Map<String, Object> queryById(Integer id);

    List<Map<String, Object>> query();

    int existTable(@Param("tableName") String tableName);

    int dropTable(@Param("tableName") String tableName);

    void truncateTable(@Param("tableName") String tableName);

    int copyTableDdl(@Param("targetTableName") String targetTableName, @Param("sourceTableName") String sourceTableName);

    int copyAllTableData(@Param("targetTableName") String targetTableName, @Param("sourceTableName") String sourceTableName);

    int copyTableByTemplate(String targetTableName, List<ReportTemplateField> targetFields, String sourceTableName, List<ReportTemplateField> sourceFields, String sourceCondition);

    int createCustomTable(String tableName, List<ReportTemplateField> customTables);

    int cusInsert(String tableName, String tableInsertFields, String tableInsertFieldValues);


}