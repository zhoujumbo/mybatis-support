package com.cuckoo.entity;

import java.util.List;
import java.util.Map;

public class InsertTrackpoint {

    private String tableName;

    private List<String> params;

    private Map<String, Object> value;





    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    public Map<String, Object> getValue() {
        return value;
    }

    public void setValue(Map<String, Object> value) {
        this.value = value;
    }
}
