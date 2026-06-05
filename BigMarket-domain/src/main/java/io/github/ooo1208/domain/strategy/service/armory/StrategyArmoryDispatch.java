package io.github.ooo1208.domain.strategy.service.armory;

import io.github.ooo1208.domain.strategy.model.entity.StrategyAwardEntity;
import io.github.ooo1208.domain.strategy.model.entity.StrategyEntity;
import io.github.ooo1208.domain.strategy.model.entity.StrategyRuleEntity;
import io.github.ooo1208.domain.strategy.repository.IStrategyRepository;
import io.github.ooo1208.types.enums.ResponseCode;
import io.github.ooo1208.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.*;

/**
 * @author ooo1208
 * @description 策略装配库（兵工厂），负责初始化策略计算
 * @create 2026-06-03
 */

@Slf4j
@Service
public class StrategyArmoryDispatch implements IStrategyArmory, IStrategyDispatch{

    @Resource
    private IStrategyRepository repository;

    @Override
    public Boolean assembleLotteryStrategy(Long strategyId) {
        // 1. 查询并构建 策略奖品列表
        List<StrategyAwardEntity> strategyAwardEntities = repository.queryStrategyAwardList(strategyId);

        // 2. 构建 基础全局概率查找表 （查询所有奖品并生成全局概率表）
        assembleLotteryStrategy(String.valueOf(strategyId), strategyAwardEntities);

        // 3. 构建 权重规则概率查找表 （根据消费档位生成多个概率表）
        StrategyEntity strategyEntity = repository.queryStrategyEntityByStrategyId(strategyId);// 查询对应的策略实体出来
        String ruleWeight = strategyEntity.getRuleWeight(); // 获取对应的权重规则
        if (ruleWeight == null) return true; // 如果没有权重规则，则直接返回
        StrategyRuleEntity strategyRuleEntity = repository.queryStrategyRule(strategyId, ruleWeight);
        if (null == strategyRuleEntity) {
            throw new AppException(ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getCode(), ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getInfo());
        }
        Map<String, List<Integer>> ruleWeightValueMap = strategyRuleEntity.getRuleWeightValues();
        Set<String> keys = ruleWeightValueMap.keySet();
        for (String key : keys) {
            List<Integer> ruleWeightValues = ruleWeightValueMap.get(key);
            ArrayList<StrategyAwardEntity> strategyAwardEntitiesClone = new ArrayList<>(strategyAwardEntities);
            strategyAwardEntitiesClone.removeIf(strategyAwardEntity -> !ruleWeightValues.contains(strategyAwardEntity.getAwardId()));
            assembleLotteryStrategy(String.valueOf(strategyId).concat("_").concat( key), strategyAwardEntitiesClone);
        }

        return true;
    }

    private void assembleLotteryStrategy(String key, List<StrategyAwardEntity> strategyAwardEntities) {
        // 1. 获取最小概率值
        BigDecimal minAwardRate = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        // 2. 获取概率值总和
        BigDecimal totalAwardRate = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 3. 抽奖策略范围值 = 概率值总和/最小概率值
        BigDecimal rateRange = totalAwardRate.divide(minAwardRate, 0, RoundingMode.CEILING);

        // 4. 构建概率查找表
        ArrayList<Integer> strategyAwardSearchRateTables = new ArrayList<>(rateRange.intValue());
        for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
            Integer awardId = strategyAwardEntity.getAwardId();
            BigDecimal awardRate = strategyAwardEntity.getAwardRate();
            // 计算出每一个奖品需要存放到概率查找表中的数量，并循环填充
            for (int i = 0; i < rateRange.multiply(awardRate).setScale(0, RoundingMode.CEILING).intValue(); i++) {
                strategyAwardSearchRateTables.add(awardId);
            }
        }

        // 5. 乱序
        Collections.shuffle(strategyAwardSearchRateTables);

        // 6. 构建Map类型概率查找表 （Key为索引，Value为奖品ID）
        HashMap<Integer, Integer> shuffleStrategyAwardSearchRateTable = new HashMap<>();
        for (int i = 0; i < strategyAwardSearchRateTables.size(); i++) {
            shuffleStrategyAwardSearchRateTable.put(i, strategyAwardSearchRateTables.get(i));
        }

        // 7. 缓存到Redis
        repository.storeStrategyAwardSearchRateTable(key, shuffleStrategyAwardSearchRateTable.size(), shuffleStrategyAwardSearchRateTable);
    }


    @Override
    public Integer getRandomAwardId(Long strategyId) {
        // 分布式部署下，不一定为当前应用做的策略装配。也就是值不一定会保存到本应用，而是分布式应用，所以需要从 Redis 中获取。
        int rateRange = repository.getRateRange(strategyId);
        // 通过生成的随机值，获取概率值奖品查找表的结果
        return repository.getStrategyAwardAssemble(String.valueOf(strategyId), new SecureRandom().nextInt(rateRange));
    }

    @Override
    public Integer getRandomAwardId(Long strategyId, String ruleWeightValue) {
        String key = strategyId.toString().concat("_").concat(ruleWeightValue);
        // 分布式部署下，不一定为当前应用做的策略装配。也就是值不一定会保存到本应用，而是分布式应用，所以需要从 Redis 中获取。
        int rateRange = repository.getRateRange(key);
        // 通过生成的随机值，获取概率值奖品查找表的结果
        return repository.getStrategyAwardAssemble(key, new SecureRandom().nextInt(rateRange));
    }
}
