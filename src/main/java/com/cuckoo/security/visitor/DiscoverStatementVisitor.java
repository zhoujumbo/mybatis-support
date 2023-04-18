package com.cuckoo.security.visitor;

import com.cuckoo.security.rule.IRuleMatch;
import com.cuckoo.sensitive.entity.SensitiveRule;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.StatementVisitorAdapter;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author xufeixiang
 * @Description insert、update访问器
 * @date 2022年06月09日 14:48
 */
@RequiredArgsConstructor
public class DiscoverStatementVisitor extends StatementVisitorAdapter {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    /**
     * sql语句传参数，按顺序传递，使用LinkedHashMap
     */
    private final LinkedHashMap<Integer, String> parameterMap;

    /**
     * 匹配方法缓存，提升性能
     */
    private static final Map<String, IRuleMatch> RULE_MATCH_CACHE = Maps.newHashMap();

    /**
     * update语句的相关操作
     *
     * @param update 更新操作
     */
    @Override
    public void visit(Update update) {
        discover(update.getTable(), update.getColumns());
    }

    /**
     * insert语句的相关操作
     *
     * @param insert 查询操作
     */
    @Override
    public void visit(Insert insert) {
        discover(insert.getTable(), insert.getColumns());
    }

    private void discover(Table table, List<Column> columnList) {
        String tableName = table.getName();
        List<SensitiveRule> sensitiveRuleList = fetchSensitiveRuleList(tableName);
        if (MapUtils.isEmpty(parameterMap) || CollectionUtils.isEmpty(sensitiveRuleList)) {
            return;
        }

        parameterMap.forEach((index, value) -> sensitiveRuleList.stream()
                .filter(rule -> this.matchRule(rule, value))
                .findFirst()
                .ifPresent(rule -> saveOrUpdateConfig(tableName, columnList.get(index).getColumnName(), rule, value))
        );
    }

    private boolean matchRule(SensitiveRule rule, String value) {
        String matchClass = rule.getMatchClass();
        if (StringUtils.isEmpty(matchClass) || StringUtils.isEmpty(value)) {
            return false;
        }

        IRuleMatch ruleMatch = Optional.ofNullable(RULE_MATCH_CACHE.get(matchClass))
                .orElseGet(() -> {
                    try {
                        String classPath = String.format("%s.%s", IRuleMatch.packageName(), matchClass);
                        return (IRuleMatch) ClassUtils.getClass(classPath).newInstance();
                    } catch (Exception e) {
                        log.error(e.toString(), e);
                        return null;
                    }
                });
        RULE_MATCH_CACHE.putIfAbsent(matchClass, ruleMatch);
        return null != ruleMatch && ruleMatch.match(rule, value);
    }

    /**
     * 保存或更新脱敏配置
     * 1. 根据表名和列名查询配置
     * 2. 未配置且规则状态开启则新增
     * 3. 已配置但规则状态开启则更新
     * 4. 已配置但规则状态关闭则删除
     *
     * @param tableName  表名
     * @param columnName 列名
     * @param rule       规则
     * @param value      数据
     */
    private void saveOrUpdateConfig(String tableName, String columnName, SensitiveRule rule, String value) {
        log.info("tableName: {}, columnName: {}, value: {}, rule: {}", tableName, columnName, value, rule);
//        ISensitiveConfigService configService = SpringContextUtils.getBean(ISensitiveConfigService.class);
//        LambdaQueryWrapper<SensitiveConfig> queryWrapper = new LambdaQueryWrapper<SensitiveConfig>()
//                .eq(SensitiveConfig::getTableName, tableName)
//                .eq(SensitiveConfig::getColumnName, columnName);
//        SensitiveConfig existConfig = configService.getOne(queryWrapper);
//        boolean ruleIsOpen = CommonConstant.RULE_FLAG_1.equals(rule.getStatus());
//        String ruleId = rule.getId();
//        if (null != existConfig && !ruleIsOpen) {
//            configService.removeById(existConfig.getId());
//            return;
//        }
//
//        if (ruleIsOpen) {
//            if (null == existConfig) {
//                existConfig = new SensitiveConfig().setTableName(tableName).setColumnName(columnName).setSensitiveRuleId(ruleId);
//                configService.save(existConfig);
//            } else if (!StringUtils.equals(ruleId, existConfig.getSensitiveRuleId())) {
//                configService.updateById(existConfig.setSensitiveRuleId(ruleId));
//            }
//
//            SensitiveLog sensitiveLog = new SensitiveLog().setSensitiveRuleId(ruleId).setSensitiveConfigId(existConfig.getId()).setData(value);
//            SpringContextUtils.getBean(ISensitiveLogService.class).save(sensitiveLog);
//        }
    }

    private List<SensitiveRule> fetchSensitiveRuleList(String tableName) {
//        if (StringUtils.equalsAnyIgnoreCase(tableName, ParameterUtil.CONFIG_TABLE_ARRAY)) {
//            return null;
//        }
//
//        return SpringContextUtils.getBean(ISensitiveRuleService.class).list();
        return Collections.emptyList();
    }

}
