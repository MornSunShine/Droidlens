package doridlens.metrics;

import doridlens.entity.PaprikaApp;

/**
 * Author: MaoMorn
 * Date: 2020/1/2
 * Time: 9:34
 * Description:
 */
public class NumberOfClasses extends UnaryMetric<Integer> {

    private NumberOfClasses(PaprikaApp paprikaApp, int value) {
        this.value = value;
        this.entity = paprikaApp;
        this.name = "number_of_classes";
    }

    public static NumberOfClasses createNumberOfClasses(PaprikaApp paprikaApp, int value) {
        NumberOfClasses numberOfClasses = new NumberOfClasses(paprikaApp, value);
        numberOfClasses.updateEntity();
        return numberOfClasses;
    }

}
