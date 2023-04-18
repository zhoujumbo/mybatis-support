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

public class TestJsqlparser2 {


    @Test
    public void run() {
        createSelect();
//        changeSelect();
//        singleChangeSelect();
//        createInsert();
//        createUpdate();
//        createDelete();
    }

    public static void createSelect() {
        System.out.println("==================================================创建查询====================================================");
        PlainSelect plainSelect = new PlainSelect();
        //创建查询的表
        Table table = new Table("table");
        table.setAlias(new Alias("t"));

        Table table2 = new Table("(select * from a)");
        plainSelect.setFromItem(table2);
        //创建查询的列
        List<String> selectColumnsStr = Arrays.asList("f1", "f2");


        List<SelectItem> expressionItemList = selectColumnsStr.stream().map(item -> {
            SelectExpressionItem selectExpressionItem = new SelectExpressionItem();
            selectExpressionItem.setExpression(new Column(table, item));
            return (SelectItem) selectExpressionItem;
        }).collect(Collectors.toList());

        SelectExpressionItem selectExpressionItem = new SelectExpressionItem();
        selectExpressionItem.setAlias(new Alias("count"));
        Function function = new Function();
        function.setName("count");
        ExpressionList expressionList = new ExpressionList();
        expressionList.setExpressions(Arrays.asList(new Column(table, "f1")));
        function.setParameters(expressionList);
        selectExpressionItem.setExpression(function);
        expressionItemList.add(selectExpressionItem);
        plainSelect.setSelectItems(expressionItemList);

        AtomicInteger atomicInteger = new AtomicInteger(1);
        List<Join> joinList = Stream.of(new String[2]).map(item -> {
            Join join = new Join();
            join.setLeft(true);
            Table joinTable = new Table();
            joinTable.setName("table" + atomicInteger.incrementAndGet());
            joinTable.setAlias(new Alias("t" + atomicInteger.get()));
            join.setRightItem(joinTable);
            EqualsTo equalsTo = new EqualsTo();
            equalsTo.setLeftExpression(new Column(table, "f1"));
            equalsTo.setRightExpression(new Column(joinTable, "f2"));
            join.setOnExpression(equalsTo);
            return join;
        }).collect(Collectors.toList());
        plainSelect.setJoins(joinList);


        //条件
        EqualsTo leftEqualsTo = new EqualsTo();
        leftEqualsTo.setLeftExpression(new Column(table, "f1"));
        StringValue stringValue = new StringValue("1222121");
        leftEqualsTo.setRightExpression(stringValue);
        plainSelect.setWhere(leftEqualsTo);

        EqualsTo rightEqualsTo = new EqualsTo();
        rightEqualsTo.setLeftExpression(new Column(table, "f2"));
        StringValue stringValue1 = new StringValue("122212111111");
        rightEqualsTo.setRightExpression(stringValue1);
        OrExpression orExpression = new OrExpression(leftEqualsTo, rightEqualsTo);
        plainSelect.setWhere(orExpression);

        //分组
        GroupByElement groupByElement = new GroupByElement();
        groupByElement.setGroupByExpressions(Arrays.asList(new Column(table, "f1")));
        plainSelect.setGroupByElement(groupByElement);
//        System.out.println(plainSelect);

        //排序
        OrderByElement orderByElement = new OrderByElement();
        orderByElement.setAsc(true);
        orderByElement.setExpression(new Column(table, "f1"));
        OrderByElement orderByElement1 = new OrderByElement();
        orderByElement1.setAsc(false);
        orderByElement1.setExpression(new Column(table, "f2"));

        //分页
        Limit limit = new Limit();
        limit.setRowCount(new LongValue(2));
        limit.setOffset(new LongValue(10));
        plainSelect.setLimit(limit);
        plainSelect.setOrderByElements(Arrays.asList(orderByElement, orderByElement1));
        System.out.println(plainSelect.toString());
//        System.out.println(SQLFormatterUtil.format(plainSelect.toString()));
        System.out.println("==================================================创建查询====================================================");
    }


    //在原有的sql基础上改
    public static void changeSelect() {
        System.out.println("==================================================改变原有查询====================================================");
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        try {
            Select select = (Select) (parserManager.parse(new StringReader("select * from table")));
            PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
            //创建查询的表
            Table table = new Table("table");
            table.setAlias(new Alias("t"));
            plainSelect.setFromItem(table);
            //创建查询的列
            List<String> selectColumnsStr = Arrays.asList("f1", "f2");

            List<SelectItem> expressionItemList = selectColumnsStr.stream().map(item -> {
                SelectExpressionItem selectExpressionItem = new SelectExpressionItem();
                selectExpressionItem.setExpression(new Column(table, item));
                return (SelectItem) selectExpressionItem;
            }).collect(Collectors.toList());

            SelectExpressionItem selectExpressionItem = new SelectExpressionItem();
            selectExpressionItem.setAlias(new Alias("count"));
            Function function = new Function();
            function.setName("count");
            ExpressionList expressionList = new ExpressionList();
            expressionList.setExpressions(Arrays.asList(new Column(table, "f1")));
            function.setParameters(expressionList);
            selectExpressionItem.setExpression(function);
            expressionItemList.add(selectExpressionItem);
            plainSelect.setSelectItems(expressionItemList);

            AtomicInteger atomicInteger = new AtomicInteger(1);
            List<Join> joinList = Stream.of(new String[2]).map(item -> {
                Join join = new Join();
                join.setLeft(true);
                Table joinTable = new Table();
                joinTable.setName("table" + atomicInteger.incrementAndGet());
                joinTable.setAlias(new Alias("t" + atomicInteger.get()));
                join.setRightItem(joinTable);
                EqualsTo equalsTo = new EqualsTo();
                equalsTo.setLeftExpression(new Column(table, "f1"));
                equalsTo.setRightExpression(new Column(joinTable, "f2"));
                join.setOnExpression(equalsTo);
                return join;
            }).collect(Collectors.toList());
            plainSelect.setJoins(joinList);

            //条件
            EqualsTo leftEqualsTo = new EqualsTo();
            leftEqualsTo.setLeftExpression(new Column(table, "f1"));
            StringValue stringValue = new StringValue("1222121");
            leftEqualsTo.setRightExpression(stringValue);

            EqualsTo rightEqualsTo = new EqualsTo();
            rightEqualsTo.setLeftExpression(new Column(table, "f2"));
            StringValue stringValue1 = new StringValue("122212111111");
            rightEqualsTo.setRightExpression(stringValue1);
            OrExpression orExpression = new OrExpression(leftEqualsTo, rightEqualsTo);
            plainSelect.setWhere(orExpression);

            //分组
            GroupByElement groupByElement = new GroupByElement();
            groupByElement.setGroupByExpressions(Arrays.asList(new Column(table, "f1")));
            plainSelect.setGroupByElement(groupByElement);
//            System.out.println(plainSelect);

            //排序
            OrderByElement orderByElement = new OrderByElement();
            orderByElement.setAsc(true);
            orderByElement.setExpression(new Column(table, "f1"));
            OrderByElement orderByElement1 = new OrderByElement();
            orderByElement1.setAsc(false);
            orderByElement1.setExpression(new Column(table, "f2"));

            //分页
            Limit limit = new Limit();
            limit.setRowCount(new LongValue(2));
            limit.setOffset(new LongValue(10));
            plainSelect.setLimit(limit);
            plainSelect.setOrderByElements(Arrays.asList(orderByElement, orderByElement1));
            System.out.println(plainSelect.toString());
//            System.out.println(SQLFormatterUtil.format(plainSelect.toString()));
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }
        System.out.println("==================================================改变原有查询====================================================");
    }

    //在原有的sql基础上改
    public static void singleChangeSelect() {
        System.out.println("==================================================改变原有查询====================================================");
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        try {
            Select select = (Select) (parserManager.parse(new StringReader("select * from table")));
            PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
            //创建查询的表
            Table table = new Table("table");
            table.setAlias(new Alias("t"));
            plainSelect.setFromItem(table);
            //创建查询的列
            List<String> selectColumnsStr = Arrays.asList("f1", "f2", "f3", "f4");

            List<SelectItem> expressionItemList = selectColumnsStr.stream().map(item -> {
                SelectExpressionItem selectExpressionItem = new SelectExpressionItem();
                selectExpressionItem.setExpression(new Column(table, item));
                return (SelectItem) selectExpressionItem;
            }).collect(Collectors.toList());

            SelectExpressionItem selectExpressionItem = new SelectExpressionItem();
            selectExpressionItem.setAlias(new Alias("count"));
            Function function = new Function();
            function.setName("count");
            ExpressionList expressionList = new ExpressionList();
            expressionList.setExpressions(Arrays.asList(new Column(table, "f1")));
            function.setParameters(expressionList);
            selectExpressionItem.setExpression(function);
            expressionItemList.add(selectExpressionItem);
            plainSelect.setSelectItems(expressionItemList);


            //条件
            EqualsTo leftEqualsTo = new EqualsTo();
            leftEqualsTo.setLeftExpression(new Column(table, "f1"));
            StringValue stringValue = new StringValue("1222121");
            leftEqualsTo.setRightExpression(stringValue);

            EqualsTo rightEqualsTo = new EqualsTo();
            rightEqualsTo.setLeftExpression(new Column(table, "f2"));
            StringValue stringValue1 = new StringValue("122212111111");
            rightEqualsTo.setRightExpression(stringValue1);

            EqualsTo tEqualsTo = new EqualsTo();
            tEqualsTo.setLeftExpression(new Column(table, "f3"));
            StringValue stringValue3 = new StringValue("aaaa");
            tEqualsTo.setRightExpression(stringValue3);

            EqualsTo fEqualsTo = new EqualsTo();
            fEqualsTo.setLeftExpression(new Column(table, "f4"));
            StringValue stringValue4 = new StringValue("bbb");
            fEqualsTo.setRightExpression(stringValue4);

//            AndExpression andExpression = new AndExpression(leftEqualsTo);
////            AndExpression andExpression = new AndExpression(leftEqualsTo, rightEqualsTo);
////            AndExpression andExpression2 = new AndExpression(tEqualsTo, fEqualsTo);
////            OrExpression orExpression = new OrExpression(andExpression, andExpression2);
//            AndExpression andExpression = new AndExpression(leftEqualsTo, new Parenthesis(where));
            List<SqlExpressions> expressionsList = new ArrayList<>();
            SqlExpressions se1 = new SqlExpressions().setField("f1").setCondition("=").setValue(1);
            SqlExpressions se11 = new SqlExpressions().setField("f1").setCondition("!=").setValue(2);
            SqlExpressions se2 = new SqlExpressions().setField("f2").setCondition(">").setValue(3);
            SqlExpressions se3 = new SqlExpressions().setField("f3").setCondition("<=").setValue(4);
            SqlExpressions se4 = new SqlExpressions().setField("f4").setCondition("like").setValue("aa%");
            SqlExpressions se5 = new SqlExpressions().setField("f5").setCondition("between").setValue(5).setEndValue(6);
            SqlExpressions se6 = new SqlExpressions().setField("f5").setCondition("is null");
            Set<String> deps = new HashSet<>();
            deps.add("9");
            deps.add("8");
            deps.add("7");
            SqlExpressions se7 = new SqlExpressions().setField("f5").setCondition("not in").setDeptItems(deps);
//            expressionsList.addAll(Arrays.asList(se1,se11,se2,se3,se4,se5,se6,se7));
            expressionsList.addAll(Arrays.asList(se1,se11,se2,se3,se4));

            AndExpression andExpression = buildSelectWhereAndExpression(table, expressionsList);

            System.out.println(andExpression.toString());

            if(andExpression.getLeftExpression() != null){
                Expression where = plainSelect.getWhere();
                if(where != null){
                    AndExpression addExpression = new AndExpression(where, andExpression);
                    plainSelect.setWhere(addExpression);
                }else{
                    plainSelect.setWhere(andExpression);
                }
            }

            System.out.println(plainSelect.toString());
//            System.out.println(SQLFormatterUtil.format(plainSelect.toString()));
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }
        System.out.println("==================================================改变原有查询====================================================");
    }

    //创建插入sql语句
//    public static void createInsert() {
//        System.out.println("==================================================创建插入语句====================================================");
//        Insert insert = new Insert();
//        Table table = new Table();
//        table.setName("table");
//        insert.setTable(table);
//        insert.setColumns(Arrays.asList(
//                new Column(table, "f1"),
//                new Column(table, "f2"),
//                new Column(table, "f3")
//        ));
//
//        MultiExpressionList multiExpressionList = new MultiExpressionList();
//        multiExpressionList.addExpressionList(Arrays.asList(
//                new StringValue("1"),
//                new StringValue("2"),
//                new StringValue("3")
//        ));
//        insert.setItemsList(multiExpressionList);
//        System.out.println(insert);
//        System.out.println("==================================================创建插入语句====================================================");
//    }

    //创建插入sql语句
    public static void createUpdate() {
        System.out.println("==================================================创建更新语句====================================================");
        Update update = new Update();
        Table table = new Table();
        table.setName("table");
        update.setTable(table);
        update.setColumns(Arrays.asList(
                new Column(table, "f1"),
                new Column(table, "f2"),
                new Column(table, "f3")
        ));
        update.setExpressions(Arrays.asList(
                new StringValue("1"),
                new StringValue("6"),
                new StringValue("2")
        ));
        //条件
        EqualsTo leftEqualsTo = new EqualsTo();
        leftEqualsTo.setLeftExpression(new Column(table, "f1"));
        StringValue stringValue = new StringValue("1222121");
        leftEqualsTo.setRightExpression(stringValue);
        EqualsTo rightEqualsTo = new EqualsTo();
        rightEqualsTo.setLeftExpression(new Column(table, "f2"));
        StringValue stringValue1 = new StringValue("122212111111");
        rightEqualsTo.setRightExpression(stringValue1);
        OrExpression orExpression = new OrExpression(leftEqualsTo, rightEqualsTo);
        update.setWhere(orExpression);
        System.out.println(update);
        System.out.println("==================================================创建更新语句====================================================");
    }

    //创建插入sql语句
    public static void createDelete() {
        System.out.println("==================================================创建删除语句====================================================");
        Delete delete = new Delete();
        Table table = new Table();
        table.setName("table");
        delete.setTable(table);
        //条件
        EqualsTo leftEqualsTo = new EqualsTo();
        leftEqualsTo.setLeftExpression(new Column(table, "f1"));
        StringValue stringValue = new StringValue("1222121");
        leftEqualsTo.setRightExpression(stringValue);
        EqualsTo rightEqualsTo = new EqualsTo();
        rightEqualsTo.setLeftExpression(new Column(table, "f2"));
        StringValue stringValue1 = new StringValue("122212111111");
        rightEqualsTo.setRightExpression(stringValue1);
        OrExpression orExpression = new OrExpression(leftEqualsTo, rightEqualsTo);
        delete.setWhere(orExpression);
        System.out.println(delete);
        System.out.println("==================================================创建删除语句====================================================");
    }


    private Expression buildSelectWhereOrxpression(List<String> expressions){
        OrExpression orExpression = new OrExpression(null,null);
        expressions.stream().filter(StringUtils::isNotBlank)
                .forEach(var->{
                    if(orExpression.getLeftExpression() != null && orExpression.getRightExpression() != null){
                        OrExpression aes = new OrExpression(orExpression.getLeftExpression() ,orExpression.getRightExpression());
                        orExpression.setRightExpression(aes);
                    }
                    if(orExpression.getLeftExpression() != null && orExpression.getRightExpression() == null){
                        Expression lae = orExpression.getLeftExpression();
                        orExpression.setRightExpression(lae);
                    }
                    orExpression.setLeftExpression(new StringValue(var));
                });
        if(orExpression.getRightExpression() == null) {
            return orExpression.getLeftExpression();
        }
        return orExpression;
    }

    private static AndExpression buildSelectWhereAndExpression(Table table, List<SqlExpressions> expressions){
        AndExpression andExpression = new AndExpression(null,null);
        expressions.stream().filter(Objects::nonNull)
                .forEach(var->{
                    if("=".equals(var.getCondition())){
                        EqualsTo equalsTo = new EqualsTo(); // 等于表达式
                        equalsTo.setLeftExpression(new Column(table, var.getField())); // 设置表达式左边值
                        equalsTo.setRightExpression(new StringValue(String.valueOf(var.getValue())));// 设置表达式右边值
                        addToAndExpression(andExpression, equalsTo);
                    }
                    if("!=".equals(var.getCondition()) || "<>".equals(var.getCondition())){
                        NotEqualsTo notEqualsTo = new NotEqualsTo();
                        notEqualsTo.setLeftExpression(new Column(table, var.getField())); // 设置表达式左边值
                        notEqualsTo.setRightExpression(new StringValue(String.valueOf(var.getValue())));// 设置表达式右边值
                        addToAndExpression(andExpression, notEqualsTo);
                    }
                    if(">".equals(var.getCondition())){
                        GreaterThan gt = new GreaterThan();
                        gt.setLeftExpression(new Column(table, var.getField())); // 设置表达式左边值
                        gt.setRightExpression(new StringValue(String.valueOf(var.getValue())));// 设置表达式右边值
                        addToAndExpression(andExpression, gt);
                    }

                    if(">=".equals(var.getCondition())){
                        GreaterThanEquals geq = new GreaterThanEquals();
                        geq.setLeftExpression(new Column(table, var.getField())); // 设置表达式左边值
                        geq.setRightExpression(new StringValue(String.valueOf(var.getValue())));// 设置表达式右边值
                        addToAndExpression(andExpression, geq);
                    }

                    if("<".equals(var.getCondition())){
                        MinorThan mt = new MinorThan();
                        mt.setLeftExpression(new Column(table, var.getField())); // 设置表达式左边值
                        mt.setRightExpression(new StringValue(String.valueOf(var.getValue())));// 设置表达式右边值
                        addToAndExpression(andExpression, mt);
                    }

                    if("<=".equals(var.getCondition())){
                        MinorThanEquals leq = new MinorThanEquals();// "<="
                        leq.setLeftExpression(new Column(table, var.getField())); // 设置表达式左边值
                        leq.setRightExpression(new StringValue(String.valueOf(var.getValue())));// 设置表达式右边值
                        addToAndExpression(andExpression, leq);
                    }

                    if("is null".equals(var.getCondition())){
                        IsNullExpression isNull = new IsNullExpression(); // "is null"
                        isNull.setLeftExpression(new Column(table, var.getField())); // 设置表达式左边值
                        addToAndExpression(andExpression, isNull);
                    }

                    if("is not null".equals(var.getCondition())){
                        IsNullExpression isNull = new IsNullExpression(); // "is null"
                        isNull.setNot(true);// "is not null"
                        isNull.setLeftExpression(new Column(table, var.getField())); // 设置表达式左边值
                        addToAndExpression(andExpression, isNull);
                    }

                    if("like".equals(var.getCondition())){
                        LikeExpression nlike = new LikeExpression();
                        nlike.setLeftExpression(new Column(table, var.getField())); // 设置表达式左边值
                        nlike.setRightExpression(new StringValue(String.valueOf(var.getValue())));// 设置表达式右边值
                        addToAndExpression(andExpression, nlike);
                    }

                    if("not like".equals(var.getCondition())){
                        LikeExpression nlike = new LikeExpression();
                        nlike.setNot(true); // "not like"
                        nlike.setLeftExpression(new Column(table, var.getField())); // 设置表达式左边值
                        nlike.setRightExpression(new StringValue(String.valueOf(var.getValue())));// 设置表达式右边值
                        addToAndExpression(andExpression, nlike);
                    }

                    if("between".equals(var.getCondition())){
                        Between bt = new Between();
                        bt.setLeftExpression(new Column(table, var.getField())); // 设置表达式左边值
                        bt.setBetweenExpressionStart(new StringValue(String.valueOf(var.getValue())));
                        bt.setBetweenExpressionEnd(new StringValue(String.valueOf(var.getEndValue())));
                        addToAndExpression(andExpression, bt);
                    }

                    if("not between".equals(var.getCondition())){
                        Between bt = new Between();
                        bt.setNot(true);// "not between"
                        bt.setLeftExpression(new Column(table, var.getField())); // 设置表达式左边值
                        bt.setBetweenExpressionStart(new StringValue(String.valueOf(var.getValue())));
                        bt.setBetweenExpressionEnd(new StringValue(String.valueOf(var.getEndValue())));
                        addToAndExpression(andExpression, bt);
                    }

                    if("in".equals(var.getCondition()) && var.getDeptItems()!= null && !var.getDeptItems().isEmpty()){
                        ItemsList itemsList = new ExpressionList(var.getDeptItems().stream().map(StringValue::new).collect(Collectors.toList())); // 把集合转变为JSQLParser需要的元素列表
                        InExpression inExpression = new InExpression(new Column(table, var.getField()), itemsList); // 创建IN表达式对象，传入列名及IN范围列表
                        addToAndExpression(andExpression, inExpression);
                    }

                    if("not in".equals(var.getCondition())){
                        ItemsList itemsList = new ExpressionList(var.getDeptItems().stream().map(StringValue::new).collect(Collectors.toList())); // 把集合转变为JSQLParser需要的元素列表
                        InExpression inExpression = new InExpression(new Column(table, var.getField()), itemsList); // 创建IN表达式对象，传入列名及IN范围列表
                        inExpression.setNot(true);
                        addToAndExpression(andExpression, inExpression);
                    }
                });
        if(andExpression.getRightExpression() == null) {
            EqualsTo equalsTo = new EqualsTo(); // 等于表达式
            equalsTo.setLeftExpression(new Column("1")); // 设置表达式左边值
            equalsTo.setRightExpression(new LongValue("1"));// 设置表达式右边值
            andExpression.setRightExpression(equalsTo);
        }
        return andExpression;
    }

    private static void addToAndExpression(AndExpression andExpression, Expression expression){
        if(andExpression.getLeftExpression() != null && andExpression.getRightExpression() != null){
            AndExpression aes = new AndExpression(andExpression.getLeftExpression() ,andExpression.getRightExpression());
            andExpression.setRightExpression(aes);
        }
        if(andExpression.getLeftExpression() != null && andExpression.getRightExpression() == null){
            Expression lae = andExpression.getLeftExpression();
            andExpression.setRightExpression(lae);
        }
        andExpression.setLeftExpression(expression);
    }


}
