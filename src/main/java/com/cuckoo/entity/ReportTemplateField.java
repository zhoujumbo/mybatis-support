package com.cuckoo.entity;


import java.util.Date;

public class ReportTemplateField {
    private Long id;

    private String templateCode;

    private String fieldCode;

    private String fieldType;

    private Integer fieldLength;

    private Boolean validStatus;

    private String comment;
    private String remark;

    private Boolean notEmpty;
    private String defaultValue;

    private Byte fieldSort;

    private String fieldConstraintType;

    private String fieldConstraintMatchType;

    private String fieldConstraintMatchRule;

    private Boolean deleteFlag;

    private Integer version;

    private String createAt;

    private Date createTime;

    private String updateAt;

    private Date updateTime;

    private String fieldName;

    private Boolean choose;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode == null ? null : templateCode.trim();
    }

    public String getFieldCode() {
        return fieldCode;
    }

    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode == null ? null : fieldCode.trim();
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType == null ? null : fieldType.trim();
    }

    public Integer getFieldLength() {
        return fieldLength;
    }

    public void setFieldLength(Integer fieldLength) {
        this.fieldLength = fieldLength;
    }

    public Boolean getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(Boolean validStatus) {
        this.validStatus = validStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Boolean getNotEmpty() {
        return notEmpty;
    }

    public void setNotEmpty(Boolean notEmpty) {
        this.notEmpty = notEmpty;
    }

    public Byte getFieldSort() {
        return fieldSort;
    }

    public void setFieldSort(Byte fieldSort) {
        this.fieldSort = fieldSort;
    }

    public String getFieldConstraintType() {
        return fieldConstraintType;
    }

    public void setFieldConstraintType(String fieldConstraintType) {
        this.fieldConstraintType = fieldConstraintType == null ? null : fieldConstraintType.trim();
    }

    public String getFieldConstraintMatchType() {
        return fieldConstraintMatchType;
    }

    public void setFieldConstraintMatchType(String fieldConstraintMatchType) {
        this.fieldConstraintMatchType = fieldConstraintMatchType == null ? null : fieldConstraintMatchType.trim();
    }

    public String getFieldConstraintMatchRule() {
        return fieldConstraintMatchRule;
    }

    public void setFieldConstraintMatchRule(String fieldConstraintMatchRule) {
        this.fieldConstraintMatchRule = fieldConstraintMatchRule == null ? null : fieldConstraintMatchRule.trim();
    }

    public Boolean getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt == null ? null : createAt.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt == null ? null : updateAt.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName == null ? null : fieldName.trim();
    }

    public Boolean getChoose() {
        return choose;
    }

    public void setChoose(Boolean choose) {
        this.choose = choose;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}