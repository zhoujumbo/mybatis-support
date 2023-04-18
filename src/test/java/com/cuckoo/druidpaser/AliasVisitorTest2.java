package com.cuckoo.druidpaser;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuxin
 * 2022/3/26 23:04
 */
public class AliasVisitorTest2 {


//    @Test
//    public void AliasVisitor() {
//        String sql = "select u.id as userId, u.name as userName, age as userAge from users as u where u.id = 1 and u.name = '孙悟空' limit 2,10";
//        // 解析SQL
//        SQLStatement sqlStatement = SQLUtils.parseSingleMysqlStatement(sql);
//        CustomerMySqlASTVisitorAdapter customerMySqlASTVisitorAdapter = new CustomerMySqlASTVisitorAdapter();
//        sqlStatement.accept(customerMySqlASTVisitorAdapter);
//        // 表别名:{u=users}
//        System.out.println("表别名:" + customerMySqlASTVisitorAdapter.getAliasMap());
//        // 列别名{userName=u.name, userId=u.id, userAge=age}
//        System.out.println("列别名" + customerMySqlASTVisitorAdapter.getAliasColumnMap());
//    }
}
