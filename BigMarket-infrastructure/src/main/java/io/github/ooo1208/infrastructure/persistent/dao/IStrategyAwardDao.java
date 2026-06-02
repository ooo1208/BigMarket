package io.github.ooo1208.infrastructure.persistent.dao;

import io.github.ooo1208.infrastructure.persistent.po.StrategyAward;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author ooo1208
 * @description 抽奖策略奖品明细配置 - 概率、规则 DAO
 * @create 2026/6/2
 */
@Mapper
public interface IStrategyAwardDao {

    List<StrategyAward> queryStrategyAwardList();

}
