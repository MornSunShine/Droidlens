package doridlens.metrics;

import doridlens.entity.PaprikaApp;

/**
 * Author: MaoMorn
 * Date: 2020/1/2
 * Time: 9:35
 * Description:
 */
public class NumberOfInnerClasses extends UnaryMetric<Integer> {

    private NumberOfInnerClasses(PaprikaApp paprikaApp, int value) {
        this.value = value;
        this.entity = paprikaApp;
        this.name = "number_of_inner_classes";
    }

    public static NumberOfInnerClasses createNumberOfInnerClasses(PaprikaApp paprikaApp, int value) {
        NumberOfInnerClasses numberOfInnerClasses = new NumberOfInnerClasses(paprikaApp, value);
        numberOfInnerClasses.updateEntity();
        return numberOfInnerClasses;
    }

}
