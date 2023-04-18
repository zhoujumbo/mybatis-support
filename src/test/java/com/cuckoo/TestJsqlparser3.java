package com.cuckoo;

import com.cuckoo.entity.SqlExpressions;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestJsqlparser3 {

//    @Test
//    public void test01() {
//
//        System.out.println("==================================================创建插入语句====================================================");
//        Insert insert = new Insert();
//        Table table = new Table();
//        table.setName("table");
//        insert.setTable(table);
//        insert.setColumns(Arrays.asList(
//                new Column(table, "f1"),
//                new Column(table, "f2"),
//                new Column(table, "f3"),
//                new Column(table, "f4"),
//                new Column(table, "f5")
//        ));
//
//        MultiExpressionList multiExpressionList = new MultiExpressionList();
//        multiExpressionList.addExpressionList(Arrays.asList(
//                new StringValue("1"),
//                new LongValue("2"),
//                new DoubleValue("3121212121.11"),
//                new DateValue("2022-06-26"),
//                new StringValue("3")
//        ));
//        insert.setItemsList(multiExpressionList);
//        System.out.println(insert);
//
//    }


    @Test
    public void test02() {
        Table table = new Table("table");
        table.setAlias(new Alias("t"));


        List<SqlExpressions> expressionsList1 = new ArrayList<>();
        SqlExpressions se1 = new SqlExpressions().setField("f1").setCondition("=").setValue(1);
        expressionsList1.addAll(Arrays.asList(se1));

        List<SqlExpressions> expressionsList2 = new ArrayList<>();
        Set<String> deps = new HashSet<>();
        deps.add("9");
        deps.add("8");
        deps.add("7");
        SqlExpressions se7 = new SqlExpressions().setField("f5").setCondition("not in").setDeptItems(deps);
        expressionsList2.addAll(Arrays.asList(se7));


        List<List<SqlExpressions>> expressionsList = Arrays.asList(expressionsList1, expressionsList2);
        List<String> sqlParts = expressionsList.stream()
                .map(var->{
                    Expression andExpression = buildSelectWhereAndExpression(table, var);
                    return "(" + andExpression.toString() + ")";
                }).collect(Collectors.toList());
        if(!sqlParts.isEmpty()){
            System.out.println(buildSelectWhereOrxpression(sqlParts).toString());
        }
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
                    orExpression.setLeftExpression(new Column(var));
                });
        if(orExpression.getRightExpression() == null) {
            return orExpression.getLeftExpression();
        }
        return orExpression;
    }

    private Expression buildSelectWhereAndExpression(Table table, List<SqlExpressions> expressions){
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
            return andExpression.getLeftExpression();
//            EqualsTo equalsTo = new EqualsTo(); // 等于表达式
//            equalsTo.setLeftExpression(new Column("1")); // 设置表达式左边值
//            equalsTo.setRightExpression(new LongValue("1"));// 设置表达式右边值
//            andExpression.setRightExpression(equalsTo);
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


    @Test
    public void test04() {

        System.out.println("==================================================创建查询====================================================");
        PlainSelect plainSelect = new PlainSelect();
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


        System.out.println("==================================================创建查询====================================================");

    }
}
