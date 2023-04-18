package com.cuckoo.druidparser.interceptor;

import com.cuckoo.security.plugin.EncryptPlugin;
import com.cuckoo.security.util.ParameterUtil;
import com.cuckoo.security.visitor.DiscoverStatementVisitor;
import com.cuckoo.security.visitor.SecurityStatementVisitor;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.Properties;

/**
 * @Description 加解密拦截，实现加密入库，解密模糊查询
 * @date 2022年06月06日 10:59
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class ParserInterceptor implements Interceptor {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final SecurityStatementVisitor securityVisitor = new SecurityStatementVisitor();

    private static final String MODIFY_SQL_PATH = "parameterHandler.boundSql.sql";

    @PostConstruct
    public void init() {
        securityVisitor.addSecurityPlugin(new EncryptPlugin());
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        try {
            MetaObject metaObject = SystemMetaObject.forObject(invocation.getTarget());
            String originalSql = (String) metaObject.getValue(MODIFY_SQL_PATH);
            log.info("securityVisitor intercept---originalSql: {}", originalSql);
            Statement sqlStatement = CCJSqlParserUtil.parse(originalSql);

            discoverSecurityField(metaObject, sqlStatement);

            modifyExecuteSql(metaObject, sqlStatement);
        } catch (Exception e) {
            log.error(e.toString(), e);
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object o) {
        return null;
    }

    @Override
    public void setProperties(Properties properties) {

    }

    private void discoverSecurityField(MetaObject metaObject, Statement sqlStatement) {
        LinkedHashMap<Integer, String> parameterMap = ParameterUtil.fetchParameterMap(metaObject);
        sqlStatement.accept(new DiscoverStatementVisitor(parameterMap));
    }

    private void modifyExecuteSql(MetaObject metaObject, Statement sqlStatement) {
        sqlStatement.accept(securityVisitor);
        if (securityVisitor.modify()) {
            String targetSql = sqlStatement.toString();
            log.info("securityVisitor intercept---targetSql: {}", targetSql);
            metaObject.setValue(MODIFY_SQL_PATH, targetSql);
        }
    }
}
