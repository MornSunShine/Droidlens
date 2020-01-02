package doridlens.metrics;

import doridlens.entity.PaprikaMethod;

/**
 * Author: MaoMorn
 * Date: 2020/1/2
 * Time: 9:28
 * Description:
 */
public class IsGetter extends UnaryMetric<Boolean> {

    private IsGetter(PaprikaMethod entity, boolean value) {
        this.value = value;
        this.entity = entity;
        this.name = "is_getter";
    }

    public static IsGetter createIsGetter(PaprikaMethod entity, boolean value) {
        IsGetter isGetter = new IsGetter(entity, value);
        isGetter.updateEntity();
        return isGetter;
    }
}
