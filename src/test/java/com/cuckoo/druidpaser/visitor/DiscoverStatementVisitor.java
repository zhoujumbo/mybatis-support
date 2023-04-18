package com.cuckoo.druidpaser.visitor;

import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlDeleteStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUpdateStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;

public class DiscoverStatementVisitor extends MySqlASTVisitorAdapter {


    @Override
    public void endVisit(MySqlDeleteStatement x) {
    }

    @Override
    public boolean visit(MySqlDeleteStatement x) {
        return true;
    }

    @Override
    public void endVisit(MySqlInsertStatement x) {
    }

    @Override
    public boolean visit(MySqlInsertStatement x) {
        return true;
    }


    @Override
    public boolean visit(MySqlUpdateStatement x) {
        return true;
    }

    @Override
    public void endVisit(MySqlUpdateStatement x) {
    }

    @Override
    public boolean visit(MySqlSelectQueryBlock x) {
        return this.visit((SQLSelectQueryBlock)x);
    }

    @Override
    public void endVisit(MySqlSelectQueryBlock x) {
        this.endVisit((SQLSelectQueryBlock)x);
    }



}
