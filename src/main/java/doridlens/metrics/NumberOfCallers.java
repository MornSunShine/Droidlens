package doridlens.metrics;

import doridlens.entity.PaprikaMethod;

/**
 * Author: MaoMorn
 * Date: 2020/1/2
 * Time: 9:33
 * Description:
 */
public class NumberOfCallers extends UnaryMetric<Integer> {

    private NumberOfCallers(PaprikaMethod paprikaMethod, int value) {
        this.value = value;
        this.entity = paprikaMethod;
        this.name = "number_of_callers";
    }

    public static NumberOfCallers createNumberOfCallers(PaprikaMethod paprikaMethod, int value) {
        NumberOfCallers numberOfCallers = new NumberOfCallers(paprikaMethod, value);
        numberOfCallers.updateEntity();
        return numberOfCallers;
    }
}
