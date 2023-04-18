package com.cuckoo.security.rule;

import com.cuckoo.sensitive.entity.SensitiveRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xufeixiang
 * @Description TODO
 * @date 2022年06月16日 14:40
 */
public class SqlRuleMatch implements IRuleMatch {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private static final String SQL_RULE_MATCH_KEY = "sql_rule_match_sql";

    /**
     * 匹配脱敏规则，并对正则表达式做缓存提升性能
     *
     * @param rule  脱敏规则
     * @param value 匹配值
     * @return 是否匹配
     */
    @Override
    public boolean match(SensitiveRule rule, String value) {
//        String matchSql = rule.getMatchParam();
//        if (StringUtils.isEmpty(matchSql)) {
//            return false;
//        }
//
//        RedisUtil redisUtil = SpringContextUtils.getBean(RedisUtil.class);
//        List<String> matchRecord;
//        if (redisUtil.hHasKey(SQL_RULE_MATCH_KEY, matchSql)) {
//            matchRecord = JSON.parseArray(redisUtil.hget(SQL_RULE_MATCH_KEY, matchSql).toString(), String.class);
//        } else {
//            matchRecord = SpringContextUtils.getBean(SensitiveRuleMapper.class).selectMatchRecord(matchSql);
//            redisUtil.hset(SQL_RULE_MATCH_KEY, matchSql, JSON.toJSONString(matchRecord), 10 * 60 * 1000);
//        }
//        boolean matchResult = Stream.of(StringUtils.split(value, StringPool.COMMA)).anyMatch(matchRecord::contains);
//        log.info("value: {}, matchSql: {}, matchResult: {}", value, matchSql, matchResult);
//        return matchResult;
        return true;
    }
}
