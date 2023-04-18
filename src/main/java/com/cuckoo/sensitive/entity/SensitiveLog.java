package com.cuckoo.sensitive.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 脱敏记录
 * @Author: jeecg-boot
 * @Date: 2022-06-16
 * @Version: V1.0
 */
//@Data
//@TableName("sensitive_log")
//@Accessors(chain = true)
//@EqualsAndHashCode(callSuper = false)
//@ApiModel(value = "sensitive_log对象", description = "脱敏记录")
public class SensitiveLog implements Serializable {
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
    private java.util.Date createTime;
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
    private java.util.Date updateTime;
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
     * 脱敏配置ID
     */
//    @Excel(name = "脱敏配置ID", width = 15)
//    @ApiModelProperty(value = "脱敏配置ID")
    private String sensitiveConfigId;
    /**
     * 捕获数据
     */
//    @Excel(name = "捕获数据", width = 15)
//    @ApiModelProperty(value = "捕获数据")
    private String data;
    /**
     * 脱敏规则ID
     */
//    @Excel(name = "脱敏规则ID", width = 15)
//    @ApiModelProperty(value = "脱敏规则ID")
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

    public String getSensitiveConfigId() {
        return sensitiveConfigId;
    }

    public void setSensitiveConfigId(String sensitiveConfigId) {
        this.sensitiveConfigId = sensitiveConfigId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getSensitiveRuleId() {
        return sensitiveRuleId;
    }

    public void setSensitiveRuleId(String sensitiveRuleId) {
        this.sensitiveRuleId = sensitiveRuleId;
    }
}
