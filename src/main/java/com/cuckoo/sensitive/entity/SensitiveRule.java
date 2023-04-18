package com.cuckoo.sensitive.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * @Description: 脱敏规则
 * @Author: jeecg-boot
 * @Date: 2022-06-14
 * @Version: V1.0
 */
//@Data
//@TableName("sensitive_rule")
//@Accessors(chain = true)
//@EqualsAndHashCode(callSuper = false)
//@ApiModel(value = "sensitive_rule对象", description = "脱敏规则")
public class SensitiveRule implements Serializable {
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
//    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
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
//    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
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
//    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    private String sysOrgCode;
    /**
     * 逻辑删除
     */
//    @Excel(name = "逻辑删除", width = 15)
//    @TableLogic
//    @ApiModelProperty(value = "逻辑删除")
    private Integer delFlag;
    /**
     * 规则分类
     */
//    @Excel(name = "规则分类", width = 15)
//    @ApiModelProperty(value = "规则分类")
//    @Dict(dicCode = "sensitive_rule_type")
    private Integer type;
    /**
     * 规则名称
     */
//    @Excel(name = "规则名称", width = 15)
//    @ApiModelProperty(value = "规则名称")
    private String name;
    /**
     * 敏感等级
     */
//    @Excel(name = "敏感等级", width = 15)
//    @ApiModelProperty(value = "敏感等级")
//    @Dict(dicCode = "sensitive_rule_level")
    private String level;
    /**
     * 状态
     */
//    @Excel(name = "状态", width = 15)
//    @ApiModelProperty(value = "状态")
    private Integer status;
    /**
     * 来源
     */
//    @Excel(name = "来源", width = 15)
//    @ApiModelProperty(value = "来源")
    private String origin;
    /**
     * 描述
     */
//    @Excel(name = "描述", width = 15)
//    @ApiModelProperty(value = "描述")
    private String description;
    /**
     * 匹配类名
     */
//    @Excel(name = "匹配类名", width = 15)
//    @ApiModelProperty(value = "匹配类名")
//    @Dict(dicCode = "sensitive_match_class")
    private String matchClass;
    /**
     * 匹配入参
     */
//    @Excel(name = "匹配入参", width = 15)
//    @ApiModelProperty(value = "匹配入参")
    private String matchParam;
    /**
     * 插件类名
     */
//    @Excel(name = "插件类名", width = 15)
//    @ApiModelProperty(value = "插件类名")
//    @Dict(dicCode = "sensitive_plugin_class")
    private String pluginClass;
    /**
     * 插件参数
     */
//    @Excel(name = "插件参数", width = 15)
//    @ApiModelProperty(value = "插件参数")
    private String pluginParam;

    /**
     * 插件JSON参数
     */
//    @Excel(name = "插件JSON参数", width = 15)
//    @ApiModelProperty(value = "插件JSON参数")
//    @TableField(exist = false)
    private Map<String, Object> pluginJsonParam;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMatchClass() {
        return matchClass;
    }

    public void setMatchClass(String matchClass) {
        this.matchClass = matchClass;
    }

    public String getMatchParam() {
        return matchParam;
    }

    public void setMatchParam(String matchParam) {
        this.matchParam = matchParam;
    }

    public String getPluginClass() {
        return pluginClass;
    }

    public void setPluginClass(String pluginClass) {
        this.pluginClass = pluginClass;
    }

    public String getPluginParam() {
        return pluginParam;
    }

    public void setPluginParam(String pluginParam) {
        this.pluginParam = pluginParam;
    }

    public Map<String, Object> getPluginJsonParam() {
        return pluginJsonParam;
    }

    public void setPluginJsonParam(Map<String, Object> pluginJsonParam) {
        this.pluginJsonParam = pluginJsonParam;
    }
}
