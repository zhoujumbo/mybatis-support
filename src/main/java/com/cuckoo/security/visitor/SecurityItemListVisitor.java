package com.cuckoo.security.visitor;

import com.cuckoo.security.plugin.AbstractSecurityPlugin;
import com.cuckoo.sensitive.entity.SensitiveConfig;
import lombok.RequiredArgsConstructor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitorAdapter;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

/**
 * @author xufeixiang
 * @Description 参数访问器，处理占位符嵌套加密函数
 * @date 2022年06月09日 14:48
 */
@RequiredArgsConstructor
public class SecurityItemListVisitor extends ItemsListVisitorAdapter {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    /**
     * 安全插件
     */
    private final AbstractSecurityPlugin plugin;
    /**
     * 加密字段对应参数占位符'?'的<位置,配置></位置,>列表
     */
    private final LinkedHashMap<Integer, SensitiveConfig> jdbcParameterConfig;

    @Override
    public void visit(ExpressionList expressionList) {
        setJdbcParameterForPlaceHolder(plugin, jdbcParameterConfig, expressionList.getExpressions());
    }

    protected static void setJdbcParameterForPlaceHolder(AbstractSecurityPlugin plugin,
                                                         LinkedHashMap<Integer, SensitiveConfig> jdbcParameterConfig,
                                                         List<Expression> expressions) {
        if (MapUtils.isEmpty(jdbcParameterConfig)) {
            return;
        }

        // 将对应位置的占位符嵌套上加密函数
        jdbcParameterConfig.forEach((index, config) ->
                Optional.ofNullable(plugin.getJdbcParameterFormat(config))
                        .ifPresent(format -> {
                            plugin.addModifyParamIndex(index);
                            expressions.set(index, new EncryptJdbcParameter(format));
                        })
        );
    }

    @RequiredArgsConstructor
    static class EncryptJdbcParameter extends JdbcParameter {

        private final String jdbcParameterFormat;

        @Override
        public String toString() {
            return jdbcParameterFormat;
        }
    }
}
