package com.cuckoo.security.util;

import cn.hutool.core.annotation.AnnotationUtil;
import com.cuckoo.sensitive.entity.SensitiveConfig;
import com.cuckoo.sensitive.entity.SensitiveLog;
import com.cuckoo.sensitive.entity.SensitiveRule;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.reflection.ArrayUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.sql.Array;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author xufeixiang
 * @Description TODO
 * @date 2022年06月14日 09:35
 */
public class ParameterUtil {

    public static final String[] CONFIG_TABLE_ARRAY = new String[]{
//            AnnotationUtil.getAnnotationValue(SensitiveRule.class, TableName.class),
//            AnnotationUtil.getAnnotationValue(SensitiveConfig.class, TableName.class),
//            AnnotationUtil.getAnnotationValue(SensitiveLog.class, TableName.class),
            "sys_log"
    };

    private static final String TYPE_HANDLER_PATH = "parameterHandler.typeHandlerRegistry";
    private static final String CONFIGURATION_PATH = "parameterHandler.configuration";
    private static final String BOUND_SQL_PATH = "parameterHandler.boundSql";

    public static String objectValueString(Object value) {
        if (null == value) {
            return null;
        }

        if (value instanceof Array) {
            try {
                return ArrayUtil.toString(((Array) value).getArray());
            } catch (SQLException var3) {
                return value.toString();
            }
        } else {
            return value.toString();
        }
    }

    public static LinkedHashMap<Integer, String> fetchParameterMap(MetaObject metaObject) {
        BoundSql boundSql = (BoundSql) metaObject.getValue(BOUND_SQL_PATH);
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        Object parameterObject = boundSql.getParameterObject();
        if (null == parameterMappings) {
            return null;
        }

        LinkedHashMap<Integer, String> parameterMap = new LinkedHashMap<>(8);
        TypeHandlerRegistry typeHandlerRegistry = (TypeHandlerRegistry) metaObject.getValue(TYPE_HANDLER_PATH);
        MetaObject parameterMeta = null;
        for (int i = 0; i < parameterMappings.size(); ++i) {
            ParameterMapping parameterMapping = parameterMappings.get(i);
            if (parameterMapping.getMode() != ParameterMode.OUT) {
                String propertyName = parameterMapping.getProperty();
                Object value;
                if (boundSql.hasAdditionalParameter(propertyName)) {
                    value = boundSql.getAdditionalParameter(propertyName);
                } else if (null == parameterObject) {
                    value = null;
                } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                    value = parameterObject;
                } else {
                    if (null == parameterMeta) {
                        Configuration configuration = (Configuration) metaObject.getValue(CONFIGURATION_PATH);
                        parameterMeta = configuration.newMetaObject(parameterObject);
                    }
                    value = parameterMeta.getValue(propertyName);
                }
                parameterMap.put(i, objectValueString(value));
            }
        }
        return parameterMap;
    }

}
