package doridlens.analyse.metrics.classes.stat.paprika;

import doridlens.analyse.entities.PaprikaClass;
import doridlens.analyse.metrics.UnaryMetric;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 21:06
 * Description: Must be processed after class complexity.
 */
public class NPathComplexity implements PaprikaClassStatistic {

    @Override
    public void collectMetric(PaprikaClass paprikaClass) {
        UnaryMetric<Double> metric = new UnaryMetric<>("npath_complexity", paprikaClass,
                paprikaClass.computeNPathComplexity());
        metric.updateEntity();
    }
}
