package com.cuckoo.security.visitor;

import cn.hutool.core.util.StrUtil;
import com.cuckoo.security.plugin.AbstractSecurityPlugin;
import com.cuckoo.sensitive.entity.SensitiveConfig;
import com.google.common.collect.Lists;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.StatementVisitorAdapter;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author xufeixiang
 * @Description insert、delete、update、select访问器
 * @date 2022年06月09日 14:48
 */
public class SecurityStatementVisitor extends StatementVisitorAdapter {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final List<AbstractSecurityPlugin> securityPluginList = Lists.newArrayList();

    public void addSecurityPlugin(AbstractSecurityPlugin plugin) {
        securityPluginList.add(plugin);
    }

    public boolean modify() {
        return securityPluginList.stream().anyMatch(plugin ->
                CollectionUtils.isNotEmpty(plugin.getModifyColumnList())
                        || CollectionUtils.isNotEmpty(plugin.getModifyParamIndexList())
        );
    }

    /**
     * 给where的表达式语句设置表达式访问器，并设置加密字段列表
     *
     * @param securityList 配置字段列表
     * @param where        where的表达式语句
     */
    protected static void acceptWhereVisitor(AbstractSecurityPlugin plugin, Expression where, List<SensitiveConfig> securityList) {
        if (ObjectUtils.allNotNull(securityList, where)) {
            where.accept(new SecurityExpressionVisitor(plugin, false, securityList));
        }
    }

    /**
     * 根据表的名称匹配查询出的配置并获取加密字段
     * 如果表有别名，给字段拼上别名前缀
     *
     * @param table  表
     * @param plugin 安全配置插件
     * @return 加密字段列表
     */
    protected static List<SensitiveConfig> fetchSecurityColumnList(Table table, AbstractSecurityPlugin plugin) {
        String tableName = table.getName();
        boolean isIgnoreTable = plugin.getIgnoreTableSet().stream()
                .anyMatch(ignore -> StringUtils.equalsIgnoreCase(StrUtil.toUnderlineCase(ignore), tableName));
        if (isIgnoreTable) {
            return null;
        }

        Map<String, List<SensitiveConfig>> securityConfig = plugin.getSecurityConfig();
        if (MapUtils.isEmpty(securityConfig)) {
            return null;
        }

        String tableAlias = Optional.ofNullable(table.getAlias()).map(Alias::getName).orElse(null);
        return securityConfig.keySet().stream()
                .filter(key -> StringUtils.equalsIgnoreCase(key, tableName))
                .map(securityConfig::get)
                .findAny()
                .map(list -> plugin.saveTableAliasForColumn(tableAlias, list))
                .orElse(null);
    }

    /**
     * 根据列名位置找出占位符'?'的位置
     *
     * @param tableColumnList    字段列表
     * @param securityColumnList 配置字段列表
     * @return 占位符'?'的位置列表
     */
    private LinkedHashMap<Integer, SensitiveConfig> fetchJdbcParameterConfig(List<Column> tableColumnList,
                                                                    List<SensitiveConfig> securityColumnList) {
        if (CollectionUtils.isEmpty(securityColumnList)) {
            return null;
        }

        LinkedHashMap<Integer, SensitiveConfig> jdbcParameterConfig = new LinkedHashMap<>();
        // 因为拦截器拦截到SQL参数都是占位符，所以需要根据列名位置找出占位符'?'的位置
        for (int i = 0, n = tableColumnList.size(); i < n; i++) {
            int finalI = i;
            String columnName = tableColumnList.get(i).getName(true);
            securityColumnList.stream()
                    .filter(x -> StringUtils.equalsIgnoreCase(x.getColumnName(), columnName))
                    .findAny()
                    .ifPresent(config -> jdbcParameterConfig.put(finalI, config));
        }
        return jdbcParameterConfig;
    }

    /**
     * select语句的相关操作
     *
     * @param select 查询操作
     */
    @Override
    public void visit(Select select) {
        for (AbstractSecurityPlugin plugin : securityPluginList) {
            if (null == plugin.getSelectConfig()) {
                continue;
            }

            SelectBody selectBody = select.getSelectBody();
            if (!(selectBody instanceof PlainSelect)) {
                return;
            }

            SecuritySelectVisitor selectVisitor = new SecuritySelectVisitor(plugin);
            selectBody.accept(selectVisitor);
        }
    }

    /**
     * delete语句的相关操作
     *
     * @param delete 删除操作
     */
    @Override
    public void visit(Delete delete) {
        AbstractSecurityPlugin.SecurityMethodConfig deleteConfig;
        for (AbstractSecurityPlugin plugin : securityPluginList) {
            if (null == (deleteConfig = plugin.getDeleteConfig()) || !deleteConfig.isModifyWhere()) {
                continue;
            }

            List<SensitiveConfig> securityColumnList = fetchSecurityColumnList(delete.getTable(), plugin);
            if (CollectionUtils.isEmpty(securityColumnList)) {
                continue;
            }

            acceptWhereVisitor(plugin, delete.getWhere(), securityColumnList);
        }
    }

    /**
     * update语句的相关操作
     *
     * @param update 更新操作
     */
    @Override
    public void visit(Update update) {
        AbstractSecurityPlugin.SecurityMethodConfig updateConfig;
        for (AbstractSecurityPlugin plugin : securityPluginList) {
            if (null == (updateConfig = plugin.getUpdateConfig())) {
                continue;
            }

            if (!updateConfig.isModifyWhere() && !updateConfig.isModifyUpdateParameter()) {
                continue;
            }

            List<SensitiveConfig> securityColumnList = fetchSecurityColumnList(update.getTable(), plugin);
            if (CollectionUtils.isEmpty(securityColumnList)) {
                continue;
            }

            if (updateConfig.isModifyUpdateParameter()) {
                // 给set的每个一个字段对应的占位符嵌套加密函数
                LinkedHashMap<Integer, SensitiveConfig> jdbcParameterConfig = fetchJdbcParameterConfig(update.getColumns(), securityColumnList);
                SecurityItemListVisitor.setJdbcParameterForPlaceHolder(plugin, jdbcParameterConfig, update.getExpressions());
            }
            if (updateConfig.isModifyWhere()) {
                acceptWhereVisitor(plugin, update.getWhere(), securityColumnList);
            }
        }
    }

    /**
     * insert语句的相关操作
     *
     * @param insert 查询操作
     */
    @Override
    public void visit(Insert insert) {
        AbstractSecurityPlugin.SecurityMethodConfig insertConfig;
        for (AbstractSecurityPlugin plugin : securityPluginList) {
            if (null == (insertConfig = plugin.getInsertConfig()) || !insertConfig.isModifyInsertParameter()) {
                continue;
            }

            List<SensitiveConfig> securityColumnList = fetchSecurityColumnList(insert.getTable(), plugin);
            if (CollectionUtils.isEmpty(securityColumnList)) {
                continue;
            }

            LinkedHashMap<Integer, SensitiveConfig> jdbcParameterConfig = fetchJdbcParameterConfig(insert.getColumns(), securityColumnList);
            if (MapUtils.isEmpty(jdbcParameterConfig)) {
                continue;
            }

            insert.getItemsList().accept(new SecurityItemListVisitor(plugin, jdbcParameterConfig));
        }
    }

    public List<AbstractSecurityPlugin> getSecurityPluginList() {
        return securityPluginList;
    }
}
