package com.cuckoo.plugins;


import com.cuckoo.util.ReportAdapterUtils;
import com.cuckoo.util.ReportBooster;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMap;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
public class DataScopeInterceptor extends MyMybatisInterceptorAdapter {
    private final static Logger log = LoggerFactory.getLogger(DataScopeInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        log.info("----MybatisInterceptor Interceptor------");

        MetaObject metaObject = SystemMetaObject.forObject(invocation);

        String methodName = (String) metaObject.getValue("method.name");
        System.out.println("MybatisInterceptor>>>>>methodName=>>>>"+ methodName);

        String sql = (String) metaObject.getValue("target.delegate.boundSql.sql");
        System.out.println("MybatisInterceptor>>>>>sql=>>>>"+ sql);

        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("target.delegate.mappedStatement");
        String id = mappedStatement.getId();
        System.out.println("MybatisInterceptor>>>>id==>>>>>"+ Optional.ofNullable(mappedStatement.getId()).orElse("NUL"));

        ParameterMap parameterMap = mappedStatement.getParameterMap();
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();


        if(sql.trim().toLowerCase().startsWith("select")){
            selectParser(metaObject);
//            metaObject.setValue("target.delegate.boundSql.sql", newSql);
        }

        if(sql.trim().toLowerCase().startsWith("insert")){
            CCJSqlParserManager parserManager = new CCJSqlParserManager();
            Insert insert = (Insert) parserManager.parse(new StringReader(sql));
        }

        if(sql.trim().toLowerCase().startsWith("update")){
            CCJSqlParserManager parserManager = new CCJSqlParserManager();
            Update update = (Update) parserManager.parse(new StringReader(sql));
        }

        if(sql.trim().toLowerCase().startsWith("delete")){
            CCJSqlParserManager parserManager = new CCJSqlParserManager();
            Delete delete = (Delete) parserManager.parse(new StringReader(sql));
        }

        return invocation.proceed();
    }






    @Override
    public void setProperties(Properties properties) {

    }


    private void selectParser(MetaObject metaObject) throws JSQLParserException {
        String sql = (String) metaObject.getValue("target.delegate.boundSql.sql");

        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        Select select = (Select) parserManager.parse(new StringReader(sql));

        // 是否增强处理
        ReportBooster booster = ReportAdapterUtils.get();
        if(booster != null){
            PlainSelect plainSelect = (PlainSelect) select.getSelectBody();

            Table table = new Table(booster.getTableName());
            table.setAlias(new Alias("t"));
            plainSelect.setFromItem(table);


            List<SelectItem> expressionItemList = booster.getFieldItems().stream().map(item -> {
                SelectExpressionItem selectExpressionItem = new SelectExpressionItem();
                selectExpressionItem.setExpression(new Column(table, item));
                return (SelectItem) selectExpressionItem;
            }).collect(Collectors.toList());
            plainSelect.setSelectItems(expressionItemList);
            System.out.println("增强转换过后的sql：：" + plainSelect.toString());

            metaObject.setValue("target.delegate.boundSql.sql", plainSelect.toString());
//            return plainSelect.toString();
        }

        // 是否增强处理
//        ReportBooster booster = ReportAdapterUtils.get();
//        if(booster != null){
//            StringBuilder buffer = new StringBuilder();
//            ExpressionDeParser expressionDeParser = new ExpressionDeParser();
//            SelectDeParser deparser = new DBSelectDeParser(expressionDeParser, buffer,booster.getSchemaName(), booster.getTableName(), booster.getFieldItems());
//            expressionDeParser.setSelectVisitor(deparser);
//            expressionDeParser.setBuffer(buffer);
//            select.getSelectBody().accept(deparser);
//
//            System.out.println("增强转换过后的sql：：" + buffer.toString());
//
//        }

//        return "";

    }

    private String selectParser2(MetaObject metaObject) throws JSQLParserException {
        String sql = (String) metaObject.getValue("target.delegate.boundSql.sql");

        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        Select select = (Select) parserManager.parse(new StringReader(sql));
//            SelectBody selectBody = select.getSelectBody();
//            PlainSelect plain = (PlainSelect) select.getSelectBody();
//            List<SelectItem> selectitems = plain.getSelectItems();
//            List<String> str_items = selectitems.stream().map(String::valueOf).collect(Collectors.toList());

//            System.out.println("str_items ==== " + str_items);
//            List<String> str_items = new ArrayList<String>();
//            if (selectitems != null) {
//                for (SelectItem selectitem : selectitems) {
//                    str_items.add(selectitem.toString());
//                }
//            }


//            TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
//            List<String> tableList = tablesNamesFinder.getTableList(select);
//            tableList.stream().forEach(System.out::println);

//            System.out.println(selectBody.toString());
        //            Table table = update.getTable();
//            String tableName = table.getName();

//            List<WithItem> withItemsList = select.getWithItemsList();



        // 是否增强处理
//        ReportBooster booster = ReportAdapterUtils.get();
//        if(booster != null){
//            StringBuilder buffer = new StringBuilder();
//            ExpressionDeParser expressionDeParser = new ExpressionDeParser();
//            SelectDeParser deparser = new DBSelectDeParser(expressionDeParser, buffer,booster.getSchemaName(), booster.getTableName(), booster.getFieldItems());
//            expressionDeParser.setSelectVisitor(deparser);
//            expressionDeParser.setBuffer(buffer);
//            select.getSelectBody().accept(deparser);
//
//            System.out.println("增强转换过后的sql：：" + buffer.toString());
//
//        }


//            String newSql = sql + " limit "+page.getFirstItem()+","+page.getPageCount();

//            metaObject.setValue("target.delegate.boundSql.sql", newSql);

        return "";
    }





    /**
     * select 处理器
     */
//    public class DBSelectDeParser extends SelectDeParser {
//
//        private String schemaNam;
//        private String newTableName;
//        private List<String> fieldItem;
//
//        public DBSelectDeParser(ExpressionVisitor expressionVisitor, StringBuilder buffer){
//            super(expressionVisitor, buffer);
//        }
//
//        public DBSelectDeParser(ExpressionVisitor expressionVisitor, StringBuilder buffer, String newTableName, List<String> fieldItem){
//            super(expressionVisitor, buffer);
//            this.fieldItem = fieldItem;
//            this.newTableName = newTableName;
//        }
//
//        public DBSelectDeParser(ExpressionVisitor expressionVisitor, StringBuilder buffer, String schemaNam, String newTableName, List<String> fieldItem){
//            super(expressionVisitor, buffer);
//            this.schemaNam = schemaNam;
//            this.newTableName = newTableName;
//            this.fieldItem = fieldItem;
//        }
//
//        @Override
//        public void visit(Table tableName) {
//            String schema = tableName.getSchemaName();
//            String table = tableName.getName();
//
//            tableName.setSchemaName("enterprise_financial");
//            tableName.setName(newTableName);
//            StringBuilder buffer = getBuffer();
//
//            buffer.append(tableName.getFullyQualifiedName());
//            Pivot pivot = tableName.getPivot();
//            if (pivot != null) {
//                pivot.accept(this);
//            }
//            Alias alias = tableName.getAlias();
//            if (alias != null) {
//                buffer.append(alias);
//            }
//        }
//
//        @Override
//        public void visit(SelectExpressionItem selectExpressionItem) {
//            StringBuilder buffer = getBuffer();
//            ExpressionVisitor visitor = getExpressionVisitor();
//            selectExpressionItem.getExpression().accept(visitor);
//            if (selectExpressionItem.getAlias() != null) {
//                this.buffer.append(selectExpressionItem.getAlias().toString());
//            }
//        }



//        public String getNewTableName() {
//            return newTableName;
//        }
//
//        public void setNewTableName(String newTableName) {
//            this.newTableName = newTableName;
//        }
//
//        public List<String> getFieldItem() {
//            return fieldItem;
//        }
//
//        public void setFieldItem(List<String> fieldItem) {
//            this.fieldItem = fieldItem;
//        }
//    }



}
