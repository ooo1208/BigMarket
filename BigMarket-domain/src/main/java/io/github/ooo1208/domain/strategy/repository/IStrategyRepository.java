package io.github.ooo1208.domain.strategy.repository;

import io.github.ooo1208.domain.strategy.model.entity.StrategyAwardEntity;
import io.github.ooo1208.domain.strategy.model.entity.StrategyEntity;
import io.github.ooo1208.domain.strategy.model.entity.StrategyRuleEntity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description 策略仓库接口
 * @create 2026-06-03
 * @author ooo1208
 */

public interface IStrategyRepository {
    // 1. 查询策略配置
    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);

    // 2. 缓存策略概率查找表
    void storeStrategyAwardSearchRateTable(String key, Integer rateRange, Map<Integer, Integer> strategyAwardSearchRateTable);

    // 3. 获取抽奖策略范围值
    int getRateRange(Long strategyId);

    int getRateRange(String key);

    // 4. 随机获取策略奖品ID
    Integer getStrategyAwardAssemble(String key, Integer rateKey);

    StrategyEntity queryStrategyEntityByStrategyId(Long strategyId);

    StrategyRuleEntity queryStrategyRule(Long strategyId, String ruleModel);

}
