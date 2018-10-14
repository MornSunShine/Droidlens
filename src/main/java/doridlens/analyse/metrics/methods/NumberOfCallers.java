package doridlens.analyse.metrics.methods;

import doridlens.analyse.entities.PaprikaMethod;
import doridlens.analyse.metrics.UnaryMetric;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 21:10
 * Description:
 */
public class NumberOfCallers extends UnaryMetric<Integer> {

    public static final String NAME = "number_of_callers";

    private NumberOfCallers(PaprikaMethod paprikaMethod, int value) {
        super(NAME, paprikaMethod, value);
    }

    public static void createMetric(PaprikaMethod paprikaMethod, int value) {
        NumberOfCallers numberOfCallers = new NumberOfCallers(paprikaMethod, value);
        numberOfCallers.updateEntity();
    }

}
