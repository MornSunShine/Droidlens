package doridlens.analyse.metrics.classes.stat.paprika;

import doridlens.analyse.entities.PaprikaClass;
import doridlens.analyse.metrics.UnaryMetric;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 21:05
 * Description:
 */
public class CouplingBetweenObjects implements PaprikaClassStatistic {

    @Override
    public void collectMetric(PaprikaClass paprikaClass) {
        UnaryMetric<Integer> metric = new UnaryMetric<>("coupling_between_object_classes", paprikaClass,
                paprikaClass.getCouplingValue());
        metric.updateEntity();
    }

}
