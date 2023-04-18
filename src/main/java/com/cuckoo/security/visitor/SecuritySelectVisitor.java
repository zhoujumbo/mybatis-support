package com.cuckoo.security.visitor;

import com.cuckoo.security.plugin.AbstractSecurityPlugin;
import com.cuckoo.sensitive.entity.SensitiveConfig;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.GroupByElement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectVisitorAdapter;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author xufeixiang
 * @Description select访问器，用于处理简单查询，嵌套子查询
 * @date 2022年06月09日 14:48
 */
@RequiredArgsConstructor
public class SecuritySelectVisitor extends SelectVisitorAdapter {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final AbstractSecurityPlugin plugin;

    @Override
    public void visit(PlainSelect plainSelect) {
        SecurityFromItemVisitor fromItemVisitor = new SecurityFromItemVisitor(plugin);
        List<Table> securityTableList = Lists.newArrayList();
        plainSelect.getFromItem().accept(fromItemVisitor);
        // 从最外层的from中获取table
        Optional.ofNullable(fromItemVisitor.getTable()).ifPresent(securityTableList::add);
        // 从所有的join中获取table
        Optional.ofNullable(plainSelect.getJoins()).ifPresent(joins -> joins.forEach(x -> {
            x.getRightItem().accept(fromItemVisitor);
            securityTableList.add(fromItemVisitor.getTable());
        }));
        if (securityTableList.isEmpty()) {
            return;
        }

        // 将表列表转换为加密/脱敏字段列表[a.column1, b.column2]
        List<SensitiveConfig> securityList = securityTableList.stream()
                .map(x -> SecurityStatementVisitor.fetchSecurityColumnList(x, plugin))
                .filter(CollectionUtils::isNotEmpty)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(securityList)) {
            return;
        }

        SecurityExpressionVisitor expressionVisitor = new SecurityExpressionVisitor(plugin, false, securityList);
        AbstractSecurityPlugin.SecurityMethodConfig selectConfig = plugin.getSelectConfig();
        // 如果有having条件，设置having表达式
        if (selectConfig.isModifyHaving()) {
            Optional.ofNullable(plainSelect.getHaving())
                    .ifPresent(having -> having.accept(expressionVisitor));
        }
        // 如果有group by条件，设置group by表达式
        if (selectConfig.isModifyGroupBy()) {
            Optional.ofNullable(plainSelect.getGroupBy())
                    .map(GroupByElement::getGroupByExpressions)
                    .ifPresent(expressions -> expressions.forEach(x -> x.accept(expressionVisitor)));
        }
        // 如果有order by条件，设置order by表达式
        if (selectConfig.isModifyOrderBy()) {
            Optional.ofNullable(plainSelect.getOrderByElements())
                    .ifPresent(orders -> orders.forEach(x -> x.getExpression().accept(expressionVisitor)));
        }
        // 如果有join条件，设置join表达式
        if (selectConfig.isModifyJoinOn()) {
            Optional.ofNullable(plainSelect.getJoins())
                    .ifPresent(joins -> joins.forEach(join -> join.getOnExpression().accept(expressionVisitor)));
        }
        // 给每个表设置where条件表达式
        if (selectConfig.isModifyWhere()) {
            securityTableList.forEach(table -> SecurityStatementVisitor.acceptWhereVisitor(plugin, plainSelect.getWhere(), securityList));
        }
        // 给最外层的查询字段设置查询字段访问器
        if (selectConfig.isModifySelectItem()) {
            plainSelect.getSelectItems().forEach(item -> item.accept(new SecuritySelectItemVisitor(plugin, securityList)));
        }
    }
}
