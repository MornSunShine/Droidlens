package doridlens.analyse.metrics.classes.stat.soot;

import doridlens.analyse.entities.PaprikaClass;
import doridlens.analyse.metrics.UnaryMetric;
import soot.SootClass;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 21:08
 * Description:
 */
public class NumberOfAttributes implements SootClassStatistic {

    public static final String NAME = "number_of_attributes";

    @Override
    public void collectMetric(SootClass sootClass, PaprikaClass paprikaClass) {
        UnaryMetric<Integer> metric = new UnaryMetric<>(NAME, paprikaClass, sootClass.getFieldCount());
        metric.updateEntity();
    }

}
