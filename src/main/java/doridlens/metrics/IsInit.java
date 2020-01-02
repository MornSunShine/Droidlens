package doridlens.metrics;

import doridlens.entity.PaprikaMethod;

/**
 * Author: MaoMorn
 * Date: 2020/1/2
 * Time: 9:28
 * Description:
 */
public class IsInit extends UnaryMetric<Boolean> {

    private IsInit(PaprikaMethod entity, boolean value) {
        this.value = value;
        this.entity = entity;
        this.name = "is_init";
    }

    public static IsInit createIsInit(PaprikaMethod entity, boolean value) {
        IsInit isInit = new IsInit(entity, value);
        isInit.updateEntity();
        return isInit;
    }
}
