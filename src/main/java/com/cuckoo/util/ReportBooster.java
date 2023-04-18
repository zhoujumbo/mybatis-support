package com.cuckoo.util;

import java.util.List;

public class ReportBooster {

    private String sqlType;

    private String schemaName;

    private String tableName;

    private List<String> fieldItems;


    public String getSqlType() {
        return sqlType;
    }

    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getFieldItems() {
        return fieldItems;
    }

    public void setFieldItems(List<String> fieldItems) {
        this.fieldItems = fieldItems;
    }
}
