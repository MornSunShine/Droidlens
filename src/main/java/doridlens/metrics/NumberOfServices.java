package doridlens.metrics;

import doridlens.entity.PaprikaApp;

/**
 * Author: MaoMorn
 * Date: 2020/1/2
 * Time: 9:37
 * Description:
 */
public class NumberOfServices extends UnaryMetric<Integer> {

    private NumberOfServices(PaprikaApp paprikaApp, int value) {
        this.value = value;
        this.entity = paprikaApp;
        this.name = "number_of_services";
    }

    public static NumberOfServices createNumberOfServices(PaprikaApp paprikaApp, int value) {
        NumberOfServices numberOfServices = new NumberOfServices(paprikaApp, value);
        numberOfServices.updateEntity();
        return numberOfServices;
    }

}
