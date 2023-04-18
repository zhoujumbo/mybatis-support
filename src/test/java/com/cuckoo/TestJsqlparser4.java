package com.cuckoo;

import com.cuckoo.entity.SqlExpressions;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.StringReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 测试 union 语句
 */
public class TestJsqlparser4 {


    @Test
    public void run() {
        singleChangeSelect();
    }

      //在原有的sql基础上改
    public static void singleChangeSelect() {
        System.out.println("==================================================改变原有查询====================================================");
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        try {

            String sql = "SELECT a.* FROM\n" +
                    "(\n" +
                    "\tSELECT id, year, month, journal_date, process_instance_id FROM general_journal WHERE journal_date >= '2022-08-03' AND journal_date <= '2022-08-03'\n" +
                    "\tUNION ALL\n" +
                    "\tSELECT id, year, month, journal_date, process_instance_id FROM general_journal_v20220825105841  WHERE journal_date >= '2022-08-03' AND journal_date <= '2022-08-03'\n" +
                    ") as a";


            Select select = (Select) (parserManager.parse(new StringReader(sql)));
            PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
            //创建查询的表
            List<SelectItem> selectItems = plainSelect.getSelectItems();
            Expression where = plainSelect.getWhere();
            FromItem fromItem = plainSelect.getFromItem();
            List<Table> intoTables = plainSelect.getIntoTables();

//            Table table = new Table("table");
//            table.setAlias(new Alias("t"));
//            plainSelect.setFromItem(table);
            //创建查询的列
            List<String> selectColumnsStr = Arrays.asList("f1", "f2");

        

            //条件
            EqualsTo leftEqualsTo = new EqualsTo();
            leftEqualsTo.setLeftExpression(new Column( "f1"));
            StringValue stringValue = new StringValue("1222121");
            leftEqualsTo.setRightExpression(stringValue);

            EqualsTo rightEqualsTo = new EqualsTo();
            rightEqualsTo.setLeftExpression(new Column( "f2"));
            StringValue stringValue1 = new StringValue("122212111111");
            rightEqualsTo.setRightExpression(stringValue1);
            OrExpression orExpression = new OrExpression(leftEqualsTo, rightEqualsTo);
            plainSelect.setWhere(orExpression);


            //排序
//            OrderByElement orderByElement = new OrderByElement();
//            orderByElement.setAsc(true);
//            orderByElement.setExpression(new Column(table, "f1"));
//            OrderByElement orderByElement1 = new OrderByElement();
//            orderByElement1.setAsc(false);
//            orderByElement1.setExpression(new Column(table, "f2"));

            //分页
//            Limit limit = new Limit();
//            limit.setRowCount(new LongValue(2));
//            limit.setOffset(new LongValue(10));
//            plainSelect.setLimit(limit);
//            plainSelect.setOrderByElements(Arrays.asList(orderByElement, orderByElement1));
            System.out.println(plainSelect.toString());
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }
        System.out.println("==================================================改变原有查询====================================================");
    }


}
