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
public class NumberOfDeclaredLocals implements MethodStatistic {

    @Override
    public void collectMetric(SootMethod sootMethod, PaprikaMethod paprikaMethod) {
        UnaryMetric<Integer> metric = new UnaryMetric<>("number_of_declared_locals", paprikaMethod,
                sootMethod.getActiveBody().getLocals().size());
        metric.updateEntity();
    }

}
