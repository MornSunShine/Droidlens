package doridlens.metrics;

import doridlens.entity.PaprikaMethod;

/**
 * Author: MaoMorn
 * Date: 2020/1/2
 * Time: 9:35
 * Description:
 */
public class NumberOfDirectCalls extends UnaryMetric<Integer> {

    private NumberOfDirectCalls(PaprikaMethod paprikaMethod, int value) {
        this.value = value;
        this.entity = paprikaMethod;
        this.name = "number_of_direct_calls";
    }

    public static NumberOfDirectCalls createNumberOfDirectCalls(PaprikaMethod paprikaMethod, int value) {
        NumberOfDirectCalls numberOfDirectCalls = new NumberOfDirectCalls(paprikaMethod, value);
        numberOfDirectCalls.updateEntity();
        return numberOfDirectCalls;
    }
}
