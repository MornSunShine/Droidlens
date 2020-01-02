package doridlens.metrics;

import doridlens.entity.PaprikaMethod;

/**
 * Author: MaoMorn
 * Date: 2020/1/2
 * Time: 9:29
 * Description:
 */
public class IsOverride extends UnaryMetric<Boolean> {

    private IsOverride(PaprikaMethod entity, boolean value) {
        this.value = value;
        this.entity = entity;
        this.name = "is_override";
    }

    public static IsOverride createIsOverride(PaprikaMethod entity, boolean value) {
        IsOverride isOverride = new IsOverride(entity, value);
        isOverride.updateEntity();
        return isOverride;
    }
}
