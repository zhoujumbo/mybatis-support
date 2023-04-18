package com.cuckoo.security.rule;


import com.cuckoo.sensitive.entity.SensitiveRule;

/**
 * @author xufeixiang
 * @Description TODO
 * @date 2022年06月16日 14:38
 */
public interface IRuleMatch {

    /**
     * 是否匹配成功
     *
     * @param rule  规则
     * @param value 捕获数据
     * @return 是否成功
     */
    boolean match(SensitiveRule rule, String value);

    /**
     * 获取包名
     *
     * @return 包名
     */
    static String packageName() {
        return IRuleMatch.class.getPackage().getName();
    }

}
