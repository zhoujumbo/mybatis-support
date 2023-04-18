package com.cuckoo.plugins.parser;

import java.util.Map;

public interface TablePicker {
    /**
     * 从sql文中挑选出所有的库表
     *
     * @param sql sql文
//     * @param mode 错误的策略模式
     * @param params
     * @return 库表列表
     */
    String pickTable(String sql, Map<String, Object> params);
}
