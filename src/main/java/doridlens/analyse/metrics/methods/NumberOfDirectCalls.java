package doridlens.analyse.metrics.methods;

import doridlens.analyse.entities.PaprikaMethod;
import doridlens.analyse.metrics.UnaryMetric;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 21:11
 * Description:
 */
public class NumberOfDirectCalls extends UnaryMetric<Integer> {

    private NumberOfDirectCalls(PaprikaMethod paprikaMethod, int value) {
        super("number_of_direct_calls", paprikaMethod, value);
    }

    public static void createMetric(PaprikaMethod paprikaMethod, int value) {
        NumberOfDirectCalls numberOfDirectCalls = new NumberOfDirectCalls(paprikaMethod, value);
        numberOfDirectCalls.updateEntity();
    }
}
