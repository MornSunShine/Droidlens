package doridlens.analyse.metrics.classes.stat.paprika;

import doridlens.analyse.entities.PaprikaClass;
import doridlens.analyse.metrics.UnaryMetric;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 21:04
 * Description:
 */
public class ClassComplexity implements PaprikaClassStatistic {

    public static final String NAME = "class_complexity";

    @Override
    public void collectMetric(PaprikaClass paprikaClass) {
        UnaryMetric<Integer> metric = new UnaryMetric<>(NAME, paprikaClass,
                paprikaClass.getComplexity());
        metric.updateEntity();
    }

}
