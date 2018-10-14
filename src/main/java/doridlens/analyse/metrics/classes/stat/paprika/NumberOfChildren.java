package doridlens.analyse.metrics.classes.stat.paprika;

import doridlens.analyse.entities.PaprikaClass;
import doridlens.analyse.metrics.UnaryMetric;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 21:06
 * Description:
 */
public class NumberOfChildren implements PaprikaClassStatistic {

    @Override
    public void collectMetric(PaprikaClass paprikaClass) {
        UnaryMetric<Integer> metric = new UnaryMetric<>("number_of_children", paprikaClass,
                paprikaClass.getChildren());
        metric.updateEntity();
    }

}
