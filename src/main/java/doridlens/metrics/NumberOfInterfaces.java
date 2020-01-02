package doridlens.metrics;

import doridlens.entity.PaprikaApp;

/**
 * Author: MaoMorn
 * Date: 2020/1/2
 * Time: 9:36
 * Description:
 */
public class NumberOfInterfaces extends UnaryMetric<Integer> {

    private NumberOfInterfaces(PaprikaApp paprikaApp, int value) {
        this.value = value;
        this.entity = paprikaApp;
        this.name = "number_of_interfaces";
    }

    public static NumberOfInterfaces createNumberOfInterfaces(PaprikaApp paprikaApp, int value) {
        NumberOfInterfaces numberOfInterfaces = new NumberOfInterfaces(paprikaApp, value);
        numberOfInterfaces.updateEntity();
        return numberOfInterfaces;
    }

}
