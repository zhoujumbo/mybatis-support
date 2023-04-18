package com.cuckoo.security.visitor;

import com.cuckoo.security.plugin.AbstractSecurityPlugin;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.FromItemVisitorAdapter;
import net.sf.jsqlparser.statement.select.SubSelect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xufeixiang
 * @Description from访问器，获取select from和join的表
 * @date 2022年06月09日 14:48
 */
@RequiredArgsConstructor
public class SecurityFromItemVisitor extends FromItemVisitorAdapter {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    @Getter
    private Table table;

    /**
     * 安全插件
     */
    private final AbstractSecurityPlugin plugin;

    @Override
    public void visit(Table table) {
        this.table = table;
    }

    @Override
    public void visit(SubSelect subSelect) {
        subSelect.getSelectBody().accept(new SecuritySelectVisitor(plugin));
    }

}
