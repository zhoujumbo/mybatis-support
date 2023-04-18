package com.cuckoo.security.plugin;

import com.cuckoo.security.util.ParameterUtil;
import com.cuckoo.security.visitor.SecurityExpressionVisitor;
import com.cuckoo.sensitive.entity.SensitiveConfig;
import com.cuckoo.sensitive.entity.SensitiveRule;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;

/**
 * @author xufeixiang
 * @Description TODO
 * @date 2022年06月11日 09:39
 */
public abstract class AbstractSecurityPlugin {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    public AbstractSecurityPlugin() {
        addAllIgnoreTable(Arrays.asList(ParameterUtil.CONFIG_TABLE_ARRAY));
    }

    /**
     * 修改的字段信息记录
     */
    private final List<String> modifyColumnList = Lists.newArrayList();

    public void addModifyColumn(String columnWithTableAlias) {
        modifyColumnList.add(columnWithTableAlias);
    }

    /**
     * 修改的占位符下标记录
     */
    private final List<Integer> modifyParamIndexList = Lists.newArrayList();

    public void addModifyParamIndex(int placeHolderIndex) {
        modifyParamIndexList.add(placeHolderIndex);
    }

    /**
     * 忽略的表名列表（驼峰会转换为下划线）
     */
    private final Set<String> ignoreTableSet = Sets.newHashSet();

    public void addAllIgnoreTable(List<String> ignoreTableList) {
        if (ignoreTableList.isEmpty()) {
            return;
        }

        ignoreTableList.stream().filter(table -> !ignoreTableSet.contains(table)).forEach(ignoreTableSet::add);
    }

    /**
     * 拦截配置{tableName: [column1, column2]}（驼峰会转换为下划线）
     *
     * @return 安全配置
     */
    public Map<String, List<SensitiveConfig>> getSecurityConfig() {
//        String existSql = String.format("select 1"
//                + " from sensitive_rule"
//                + " where id = sensitive_config.sensitive_rule_id"
//                + " and plugin_class = '%s'", this.getClass().getSimpleName());
//        return SpringContextUtils.getBean(ISensitiveConfigService.class)
//                .list(new LambdaQueryWrapper<SensitiveConfig>().exists(existSql))
//                .stream()
//                .filter(config -> StringUtils.isNotEmpty(config.getTableName()))
//                .collect(Collectors.groupingBy(SensitiveConfig::getTableName));
        return Collections.EMPTY_MAP;
    }

    /**
     * 自定义实现将表别名保存，用于后续与column匹配
     *
     * @param tableAlias       表别名
     * @param columnConfigList 根据表获取的字段配置
     */
    public List<SensitiveConfig> saveTableAliasForColumn(String tableAlias, List<SensitiveConfig> columnConfigList) {
        List<SensitiveConfig> copyList = new ArrayList<>(columnConfigList);
        copyList.forEach(config -> config.setColumnName(String.format("%s.%s", tableAlias, config.getColumnName())));
        return copyList;
    }

    /**
     * 修改字段表达式的方法
     */
    public Function<SecurityExpressionVisitor.ColumnExpression, String> getColumnExpressionFunction() {
        return null;
    }

    /**
     * 修改后占位符
     */
    public String getJdbcParameterFormat(SensitiveConfig jdbcParameterConfig) {
        return null;
    }

    public SecurityMethodConfig getInsertConfig() {
        return null;
    }

    public SecurityMethodConfig getDeleteConfig() {
        return null;
    }

    public SecurityMethodConfig getUpdateConfig() {
        return null;
    }

    public SecurityMethodConfig getSelectConfig() {
        return null;
    }

    protected SensitiveRule getSensitiveRule(String ruleId) {
        if (StringUtils.isEmpty(ruleId)) {
            return null;
        }

//        SensitiveRule rule = SpringContextUtils.getBean(ISensitiveRuleService.class).getById(ruleId);
//        if (null == rule) {
//            return null;
//        }
//
//        Optional.ofNullable(rule.getPluginParam())
//                .filter(JSONUtil::isJsonObj)
//                .ifPresent(param -> rule.setPluginJsonParam(JSONUtil.parseObj(param)));
//        return rule;
        return null;
    }

    protected SecurityMethodConfig commonInsert() {
        return SecurityMethodConfig.builder()
                .modifyInsertParameter(true)
                .build();
    }

    protected SecurityMethodConfig commonDelete() {
        return SecurityMethodConfig.builder()
                .modifyWhere(true)
                .build();
    }

    protected SecurityMethodConfig commonUpdate() {
        return SecurityMethodConfig.builder()
                .modifyWhere(true)
                .modifyUpdateParameter(true)
                .build();
    }

    protected SecurityMethodConfig commonSelect() {
        return SecurityMethodConfig.builder()
                .modifySelectItem(true)
                .modifyWhere(true)
                .modifyHaving(true)
                .modifyJoinOn(true)
                .modifyOrderBy(true)
                .modifyGroupBy(true)
                .modifyInsertParameter(true)
                .modifyUpdateParameter(true)
                .build();
    }

    protected SecurityMethodConfig simpleSelect() {
        return SecurityMethodConfig.builder()
                .modifySelectItem(true)
                .build();
    }

    @Data
    @Builder
    public static class SecurityMethodConfig {
        /**
         * 修改查询表达式
         */
        private boolean modifySelectItem;
        /**
         * 修改where表达式
         */
        private boolean modifyWhere;
        /**
         * 修改having表达式
         */
        private boolean modifyHaving;
        /**
         * 修改join on表达式
         */
        private boolean modifyJoinOn;
        /**
         * 修改order by表达式
         */
        private boolean modifyOrderBy;
        /**
         * 修改group by表达式
         */
        private boolean modifyGroupBy;
        /**
         * 修改insert占位符参数
         */
        private boolean modifyInsertParameter;
        /**
         * 修改update占位符参数
         */
        private boolean modifyUpdateParameter;
    }


    public List<String> getModifyColumnList() {
        return modifyColumnList;
    }

    public List<Integer> getModifyParamIndexList() {
        return modifyParamIndexList;
    }

    public Set<String> getIgnoreTableSet() {
        return ignoreTableSet;
    }
}
