package io.github.ooo1208.infrastructure.persistent.po;

import lombok.Data;

/**
 * @author ooo1208
 * @description 抽奖策略
 * @create 2026/6/2
 */

@Data
public class Strategy {
    /** 自增ID */
    private String id;
    /** 抽奖策略ID */
    private String strategyId;
    /** 抽奖策略描述 */
    private String strategyDesc;
    /** 规则模型，rule配置的模型同步到此表，便于使用 */
    private String ruleModels;
    /** 创建时间 */
    private String createTime;
    /** 更新时间 */
    private String updateTime;
}
/**
 * alt+Shift = 自定义选中哪几行以及每行的要复制的哪些地方
 * alt + 按住鼠标向下一拉 = 选中连续多行
 */