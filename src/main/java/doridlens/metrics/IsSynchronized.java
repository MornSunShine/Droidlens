package doridlens.metrics;

import doridlens.entity.PaprikaMethod;

/**
 * Author: MaoMorn
 * Date: 2020/1/2
 * Time: 9:30
 * Description:
 */
public class IsSynchronized extends UnaryMetric<Boolean> {

    private IsSynchronized(PaprikaMethod entity, boolean value) {
        this.value = value;
        this.entity = entity;
        this.name = "is_synchronized";
    }

    public static IsSynchronized createIsSynchronized(PaprikaMethod entity, boolean value) {
        IsSynchronized isSynchronized = new IsSynchronized(entity, value);
        isSynchronized.updateEntity();
        return isSynchronized;
    }
}
