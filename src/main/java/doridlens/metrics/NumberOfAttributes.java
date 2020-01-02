package doridlens.metrics;

import doridlens.entity.PaprikaClass;

/**
 * Author: MaoMorn
 * Date: 2020/1/2
 * Time: 9:32
 * Description:
 */
public class NumberOfAttributes extends UnaryMetric<Integer> {

    private NumberOfAttributes(PaprikaClass paprikaClass, int value) {
        this.value = value;
        this.entity = paprikaClass;
        this.name = "number_of_attributes";
    }

    public static NumberOfAttributes createNumberOfAttributes(PaprikaClass paprikaClass, int value) {
        NumberOfAttributes numberOfAttributes = new NumberOfAttributes(paprikaClass, value);
        numberOfAttributes.updateEntity();
        return numberOfAttributes;
    }

}
