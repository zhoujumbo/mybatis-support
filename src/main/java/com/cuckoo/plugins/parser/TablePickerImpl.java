package com.cuckoo.plugins.parser;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import net.sf.jsqlparser.util.deparser.SelectDeParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;
import java.util.Map;

public class TablePickerImpl implements TablePicker {

    private static Logger logger = LoggerFactory.getLogger(TablePickerImpl.class);

    @Override
    public String pickTable(String sql, Map<String, Object> params) {

        CCJSqlParserManager parser = new CCJSqlParserManager();
        StringBuilder buffer = new StringBuilder();
        try {
            Statement stmt = parser.parse(new StringReader(sql));

            // 查询语句处理
            if (stmt instanceof Select) {
                Select Statement = (Select) stmt;
                logger.debug("解析sql的语句: {} ",Statement.toString());
                //Start of value modification
                ExpressionDeParser expressionDeParser = new ExpressionDeParser();
                SelectDeParser deparser = new DBSelectDeParser(expressionDeParser, buffer);
                expressionDeParser.setSelectVisitor(deparser);
                expressionDeParser.setBuffer(buffer);
                Statement.getSelectBody().accept(deparser);
                logger.debug("替换成功，文本为: {} ", buffer.toString());
                return buffer.toString();
            }
            // 插入语句处理
            if (stmt instanceof Insert) {
                Insert Statement = (Insert) stmt;
                logger.debug("解析sql的语句: {} ",Statement.toString());

                Table t = new Table();
                t.setSchemaName("user");
                t.setName("age");
                Statement.setTable(t);

                // 获取insert语句中的查询语句，如果没有则不替换select
                Select select = Statement.getSelect();
                if (select != null) {
                    ExpressionDeParser expressionDeParser = new ExpressionDeParser();
                    SelectDeParser deparser = new DBSelectDeParser(expressionDeParser, buffer);
                    expressionDeParser.setSelectVisitor(deparser);
                    expressionDeParser.setBuffer(buffer);
                    select.getSelectBody().accept(deparser);
                }
                logger.debug("替换成功，文本为: {} ",Statement.toString());
                return Statement.toString();
            }



        } catch (JSQLParserException e) {
            logger.error(e.getMessage());
        }
        // 如果四种条件都不匹配则抛出异常。
        throw new RuntimeException("error sql can not be parse!check your sql!");

    }
}