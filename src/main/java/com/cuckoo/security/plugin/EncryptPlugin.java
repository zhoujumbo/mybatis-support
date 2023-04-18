package com.cuckoo.security.plugin;


import com.cuckoo.security.visitor.SecurityExpressionVisitor;
import com.cuckoo.sensitive.entity.SensitiveConfig;
import com.cuckoo.sensitive.entity.SensitiveRule;
import org.apache.commons.collections.MapUtils;

import java.util.Optional;
import java.util.function.Function;

/**
 * @author xufeixiang
 * @Description TODO
 * @date 2022年06月11日 09:57
 */
public class EncryptPlugin extends AbstractSecurityPlugin {

    @Override
    public SecurityMethodConfig getInsertConfig() {
        return super.commonInsert();
    }

    @Override
    public SecurityMethodConfig getDeleteConfig() {
        return super.commonDelete();
    }

    @Override
    public SecurityMethodConfig getUpdateConfig() {
        return super.commonUpdate();
    }

    @Override
    public SecurityMethodConfig getSelectConfig() {
        return super.commonSelect();
    }

    @Override
    public Function<SecurityExpressionVisitor.ColumnExpression, String> getColumnExpressionFunction() {
        return columnExpression -> {
            String decryptKey = getDecryptKey(columnExpression.getColumnConfig());
            String decryptFormat = "AES_DECRYPT(UNHEX(%s),'%s')";
            return String.format(decryptFormat, columnExpression.getColumnWithTableAlias(), decryptKey);
        };
    }

    @Override
    public String getJdbcParameterFormat(SensitiveConfig jdbcParameterConfig) {
        String decryptKey = getDecryptKey(jdbcParameterConfig);
        return String.format("HEX(AES_ENCRYPT(?,'%s'))", decryptKey);
    }

    private String getDecryptKey(SensitiveConfig jdbcParameterConfig) {
        SensitiveRule sensitiveRule = super.getSensitiveRule(jdbcParameterConfig.getSensitiveRuleId());
        return Optional.ofNullable(sensitiveRule.getPluginJsonParam())
                .map(jsonParam -> MapUtils.getString(jsonParam, "decryptKey"))
                .orElse("${decryptKey}");
    }

}
