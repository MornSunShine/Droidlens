package doridlens.metrics;

import doridlens.entity.PaprikaMethod;

/**
 * Author: MaoMorn
 * Date: 2020/1/2
 * Time: 9:37
 * Description:
 */
public class NumberOfParameters extends UnaryMetric<Integer> {

    private NumberOfParameters(PaprikaMethod paprikaMethod, int value) {
        this.value = value;
        this.entity = paprikaMethod;
        this.name = "number_of_parameters";
    }

    public static NumberOfParameters createNumberOfParameters(PaprikaMethod paprikaMethod, int value) {
        NumberOfParameters numberOfParameters = new NumberOfParameters(paprikaMethod, value);
        numberOfParameters.updateEntity();
        return numberOfParameters;
    }

}
