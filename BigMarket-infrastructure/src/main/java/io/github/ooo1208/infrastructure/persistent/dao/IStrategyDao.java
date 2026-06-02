package io.github.ooo1208.infrastructure.persistent.dao;

import io.github.ooo1208.infrastructure.persistent.po.Strategy;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author ooo1208
 * @description 抽奖策略 DAO
 * @create 2026/6/2
 */
@Mapper
public interface IStrategyDao {

    List<Strategy> queryStrategyList();

}
