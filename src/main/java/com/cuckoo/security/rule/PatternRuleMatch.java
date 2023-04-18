package com.cuckoo.security.rule;

import com.cuckoo.sensitive.entity.SensitiveRule;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @Description TODO
 * @date 2022年06月16日 14:40
 */
public class PatternRuleMatch implements IRuleMatch {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    /**
     * 正则匹配缓存，提升性能
     */
    private static final Map<String, Pattern> RULE_PATTERN_CACHE = Maps.newHashMap();

    /**
     * 匹配脱敏规则，并对正则表达式做缓存提升性能
     *
     * @param rule  脱敏规则
     * @param value 匹配值
     * @return 是否匹配
     */
    @Override
    public boolean match(SensitiveRule rule, String value) {
        String matchRegex = rule.getMatchParam();
        if (StringUtils.isEmpty(matchRegex)) {
            return false;
        }

        Pattern rulePattern = Optional.ofNullable(RULE_PATTERN_CACHE.get(matchRegex)).orElseGet(() -> Pattern.compile(matchRegex));
        RULE_PATTERN_CACHE.putIfAbsent(matchRegex, rulePattern);
        boolean matchResult = rulePattern.matcher(value).find();
        log.info("value: {}, matchRegex: {}, matchResult: {}", value, matchRegex, matchResult);
        return matchResult;
    }
}
