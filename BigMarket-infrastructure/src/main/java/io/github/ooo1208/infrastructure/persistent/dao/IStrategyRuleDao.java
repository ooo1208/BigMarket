package io.github.ooo1208.infrastructure.persistent.dao;

import io.github.ooo1208.infrastructure.persistent.po.StrategyRule;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author ooo1208
 * @description 策略规则 DAO
 * @create 2026/6/2
 */
@Mapper
public interface IStrategyRuleDao {

    List<StrategyRule> queryStrategyRuleList();

}
