package io.github.ooo1208.domain.strategy.service.armory;

/**
 * @description 策略装配库（兵工厂），负责初始化策略计算
 * @author ooo1208
 */
public interface IStrategyArmory {

    Boolean assembleLotteryStrategy(Long strategyId);

}
