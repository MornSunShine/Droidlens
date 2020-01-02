package doridlens.metrics;

import doridlens.entity.PaprikaClass;

/**
 * Author: MaoMorn
 * Date: 2020/1/2
 * Time: 9:35
 * Description:
 */
public class NumberOfImplementedInterfaces extends UnaryMetric<Integer> {

    private NumberOfImplementedInterfaces(PaprikaClass paprikaClass, int value) {
        this.value = value;
        this.entity = paprikaClass;
        this.name = "number_of_implemented_interfaces";
    }

    public static NumberOfImplementedInterfaces createNumberOfImplementedInterfaces(PaprikaClass paprikaClass, int value) {
        NumberOfImplementedInterfaces numberOfImplementedInterfaces =new NumberOfImplementedInterfaces(paprikaClass, value);
        numberOfImplementedInterfaces.updateEntity();
        return numberOfImplementedInterfaces;
    }

}
