package com.cuckoo.security.visitor;

import com.cuckoo.security.plugin.AbstractSecurityPlugin;
import com.cuckoo.sensitive.entity.SensitiveConfig;
import lombok.RequiredArgsConstructor;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItemVisitorAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author xufeixiang
 * @Description 查询字段访问器，处理select查询字段的表达式
 * @date 2022年06月09日 14:48
 */
@RequiredArgsConstructor
public class SecuritySelectItemVisitor extends SelectItemVisitorAdapter {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    /**
     * 安全插件
     */
    private final AbstractSecurityPlugin plugin;
    /**
     * 上层传递的安全字段列表
     */
    private final List<SensitiveConfig> securityColumnNameList;

    /**
     * 给查询的字段设置表达式访问器
     * appendAs = null == item.getAlias() 如果列本身有别名就不用追加别名了
     *
     * @param item 查询的每个元素
     */
    @Override
    public void visit(SelectExpressionItem item) {
        item.getExpression().accept(new SecurityExpressionVisitor(plugin, null == item.getAlias(), securityColumnNameList));
    }
}
