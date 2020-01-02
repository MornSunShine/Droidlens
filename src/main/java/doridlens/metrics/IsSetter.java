package doridlens.metrics;

import doridlens.entity.PaprikaMethod;

/**
 * Author: MaoMorn
 * Date: 2020/1/2
 * Time: 9:29
 * Description:
 */
public class IsSetter extends UnaryMetric<Boolean> {

    private IsSetter(PaprikaMethod entity, boolean value) {
        this.value = value;
        this.entity = entity;
        this.name = "is_setter";
    }

    public static IsSetter createIsSetter(PaprikaMethod entity, boolean value) {
        IsSetter isSetter = new IsSetter(entity, value);
        isSetter.updateEntity();
        return isSetter;
    }
}
