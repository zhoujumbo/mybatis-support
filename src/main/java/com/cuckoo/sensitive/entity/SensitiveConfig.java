package com.cuckoo.sensitive.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 脱敏配置 sensitive_config对象
 * @Author: jeecg-boot
 * @Date: 2022-06-14
 * @Version: V1.0
 */
//@Data
//@TableName("sensitive_config")
//@Accessors(chain = true)
//@EqualsAndHashCode(callSuper = false)
//@ApiModel(value = "sensitive_config对象", description = "脱敏配置")
public class SensitiveConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
//    @TableId(type = IdType.ASSIGN_ID)
//    @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 创建人
     */
//    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**
     * 创建日期
     */
//    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @ApiModelProperty(value = "创建日期")
    private Date createTime;
    /**
     * 更新人
     */
//    @ApiModelProperty(value = "更新人")
    private String updateBy;
    /**
     * 更新日期
     */
//    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
    /**
     * 所属部门
     */
//    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
    /**
     * 逻辑删除
     */
//    @Excel(name = "逻辑删除", width = 15)
//    @TableLogic
//    @ApiModelProperty(value = "逻辑删除")
    private Integer delFlag;
    /**
     * 表名
     */
//    @Excel(name = "表名", width = 15)
//    @ApiModelProperty(value = "表名")
    private String tableName;
    /**
     * 字段名
     */
//    @Excel(name = "字段名", width = 15)
//    @ApiModelProperty(value = "字段名")
    private String columnName;
    /**
     * 引用规则id
     */
//    @Excel(name = "引用规则id", width = 15)
//    @ApiModelProperty(value = "引用规则id")
//    @Dict(dictTable = "sensitive_rule", dicCode = "id", dicText = "name")
    private String sensitiveRuleId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getSysOrgCode() {
        return sysOrgCode;
    }

    public void setSysOrgCode(String sysOrgCode) {
        this.sysOrgCode = sysOrgCode;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getSensitiveRuleId() {
        return sensitiveRuleId;
    }

    public void setSensitiveRuleId(String sensitiveRuleId) {
        this.sensitiveRuleId = sensitiveRuleId;
    }
}
