package doridlens.metrics;

import doridlens.entity.PaprikaApp;

/**
 * Author: MaoMorn
 * Date: 2020/1/2
 * Time: 9:31
 * Description:
 */
public class NumberOfAbstractClasses extends UnaryMetric<Integer> {
    private NumberOfAbstractClasses(PaprikaApp paprikaApp, int value) {
        this.value = value;
        this.entity = paprikaApp;
        this.name = "number_of_abstract_classes";
    }

    public static NumberOfAbstractClasses createNumberOfAbstractClasses(PaprikaApp paprikaApp, int value) {
        NumberOfAbstractClasses numberOfAbstractClasses = new NumberOfAbstractClasses(paprikaApp, value);
        numberOfAbstractClasses.updateEntity();
        return numberOfAbstractClasses;
    }
}
