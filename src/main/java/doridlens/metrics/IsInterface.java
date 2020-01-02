package doridlens.metrics;

import doridlens.entity.PaprikaClass;

/**
 * Author: MaoMorn
 * Date: 2020/1/2
 * Time: 9:28
 * Description:
 */
public class IsInterface extends UnaryMetric<Boolean> {

    private IsInterface(PaprikaClass entity, boolean value) {
        this.value = value;
        this.entity = entity;
        this.name = "is_interface";
    }

    public static IsInterface createIsInterface(PaprikaClass entity, boolean value) {
        IsInterface isInterface = new IsInterface(entity, value);
        isInterface.updateEntity();
        return isInterface;
    }
}
