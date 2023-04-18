package com.cuckoo.security.visitor;

import com.cuckoo.security.plugin.AbstractSecurityPlugin;
import com.cuckoo.sensitive.entity.SensitiveConfig;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.schema.Column;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Function;

/**
 * @author xufeixiang
 * @Description 表达式访问器，包括（查询字段、where、join、having、任何运算）
 * @date 2022年06月09日 14:48
 */
@RequiredArgsConstructor
public class SecurityExpressionVisitor extends ExpressionVisitorAdapter {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    /**
     * 安全插件
     */
    private final AbstractSecurityPlugin plugin;
    /**
     * 是否追加列的别名
     */
    private final boolean appendColumnAlias;
    /**
     * 从上层传递的安全字段列表
     */
    private final List<SensitiveConfig> securityColumnList;

    /**
     * 所有逻辑运算的实现
     *
     * @param expr 逻辑表达式
     */
    @Override
    public void visitBinaryExpression(BinaryExpression expr) {
        setSelectVisitor(new SecuritySelectVisitor(plugin));
        super.visitBinaryExpression(expr);
    }

    /**
     * 给加密字段嵌套解密函数
     * 如果是select判断是否追加别名
     * 通过setTable(null)去掉多余的"a.column1"中的"a."
     *
     * @param column 字段
     */
    @Override
    public void visit(Column column) {
        Function<ColumnExpression, String> columnExpressionFunction = plugin.getColumnExpressionFunction();
        if (null == columnExpressionFunction) {
            return;
        }

        String columnWithTableAlias = column.getName(true), columnWithNotTableAlias = column.getColumnName();
        SensitiveConfig columnConfig = securityColumnList.stream()
                .filter(columnConf -> StringUtils.equalsIgnoreCase(columnConf.getColumnName(), columnWithTableAlias))
                .findAny().orElse(null);
        if (null == columnConfig) {
            return;
        }

        ColumnExpression columnExpression = new ColumnExpression(columnConfig, columnWithTableAlias, columnWithNotTableAlias);
        String modifyColumnName = columnExpressionFunction.apply(columnExpression);
        if (null == modifyColumnName) {
            return;
        }

        plugin.addModifyColumn(columnWithTableAlias);
        column.setTable(null);
        if (appendColumnAlias) {
            String columnAlias = String.format(" AS %s", columnWithNotTableAlias);
            modifyColumnName = StringUtils.appendIfMissingIgnoreCase(modifyColumnName, columnAlias);
        }
        column.setColumnName(modifyColumnName);
    }

    @Getter
    @RequiredArgsConstructor
    public static class ColumnExpression {
        private final SensitiveConfig columnConfig;
        private final String columnWithTableAlias;
        private final String columnNotTableAlias;
    }
}
