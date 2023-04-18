package com.cuckoo.entity;


import java.util.Set;

public class SqlExpressions {

    private String field;
    private String condition;
    private Object value;
    private Object endValue;
    private Set<String> deptItems;

    public String getField() {
        return field;
    }

    public SqlExpressions setField(String field) {
        this.field = field;
        return this;
    }

    public String getCondition() {
        return condition;
    }

    public SqlExpressions setCondition(String condition) {
        this.condition = condition;
        return this;
    }

    public Object getValue() {
        return value;
    }

    public SqlExpressions setValue(Object value) {
        this.value = value;
        return this;
    }

    public Object getEndValue() {
        return endValue;
    }

    public SqlExpressions setEndValue(Object endValue) {
        this.endValue = endValue;
        return this;
    }

    public Set<String> getDeptItems() {
        return deptItems;
    }

    public SqlExpressions setDeptItems(Set<String> deptItems) {
        this.deptItems = deptItems;
        return this;
    }
}
