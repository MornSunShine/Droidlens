package doridlens.metrics;

import doridlens.entity.Entity;

/**
 * Author: MaoMorn
 * Date: 2020/1/2
 * Time: 9:36
 * Description:
 */
public class NumberOfMethods extends UnaryMetric<Integer> {

    private NumberOfMethods(Entity entity, int value) {
        this.value = value;
        this.entity = entity;
        this.name = "number_of_methods";
    }

    public static NumberOfMethods createNumberOfMethods(Entity entity, int value) {
        NumberOfMethods numberOfMethods = new NumberOfMethods(entity, value);
        numberOfMethods.updateEntity();
        return numberOfMethods;
    }
}
