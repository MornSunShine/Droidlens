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
public class DepthOfInheritance implements SootClassStatistic {

    @Override
    public void collectMetric(SootClass sootClass, PaprikaClass paprikaClass) {
        UnaryMetric<Integer> metric = new UnaryMetric<>("depth_of_inheritance", paprikaClass,
                getDepthOfInheritance(sootClass));
        metric.updateEntity();
    }

    private int getDepthOfInheritance(SootClass sootClass) {
        int doi = 0;
        do {
            doi++;
            sootClass = sootClass.getSuperclass();
        } while (sootClass.hasSuperclass());
        return doi;
    }

}
