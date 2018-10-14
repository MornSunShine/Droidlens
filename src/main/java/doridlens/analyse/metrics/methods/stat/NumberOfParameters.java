package doridlens.analyse.metrics.methods.stat;

import doridlens.analyse.entities.PaprikaMethod;
import doridlens.analyse.metrics.UnaryMetric;
import soot.SootMethod;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 21:14
 * Description:
 */
public class NumberOfParameters implements MethodStatistic {

    public static final String NAME = "number_of_parameters";

    @Override
    public void collectMetric(SootMethod sootMethod, PaprikaMethod paprikaMethod) {
        UnaryMetric<Integer> metric = new UnaryMetric<>(NAME, paprikaMethod,
                sootMethod.getParameterCount());
        metric.updateEntity();
    }

}
