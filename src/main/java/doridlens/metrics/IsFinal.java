package doridlens.metrics;

import doridlens.entity.Entity;

/**
 * Author: MaoMorn
 * Date: 2020/1/2
 * Time: 9:27
 * Description:
 */
public class IsFinal extends UnaryMetric<Boolean> {

    private IsFinal(Entity entity, boolean value) {
        this.value = value;
        this.entity = entity;
        this.name = "is_final";
    }

    public static IsFinal createIsFinal(Entity entity, boolean value) {
        IsFinal isFinal = new IsFinal(entity, value);
        isFinal.updateEntity();
        return isFinal;
    }
}
