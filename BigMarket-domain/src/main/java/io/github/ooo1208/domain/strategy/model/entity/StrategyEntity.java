package io.github.ooo1208.domain.strategy.model.entity;

import io.github.ooo1208.types.common.Constants;
import jodd.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StrategyEntity {

    /** 抽奖策略ID */
    private String strategyId;
    /** 抽奖策略描述 */
    private String strategyDesc;
    /** 规则模型，rule配置的模型同步到此表，便于使用 */
    private String ruleModels;

    public String[] ruleModels(){
        if (StringUtil.isBlank(ruleModels)) return null;
        return ruleModels.split(Constants.SPLIT);
    }

    public String getRuleWeight(){
        String[] ruleModels = this.ruleModels();
        for (String ruleModel : ruleModels) {
            if ("rule_weight".equals(ruleModel)) return ruleModel;
        }
        return null;
    }

}
