package doridlens.analyse.metrics.classes.stat.soot;

import doridlens.analyse.entities.PaprikaClass;
import doridlens.analyse.metrics.UnaryMetric;
import soot.SootClass;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 21:07
 * Description:
 */
public class ImplementedInterfaces implements SootClassStatistic {

    @Override
    public void collectMetric(SootClass sootClass, PaprikaClass paprikaClass) {
        UnaryMetric<Integer> metric = new UnaryMetric<>("number_of_implemented_interfaces", paprikaClass,
                sootClass.getInterfaceCount());
        metric.updateEntity();
    }

}
