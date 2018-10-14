package doridlens.analyse.metrics.classes.stat.paprika;

import doridlens.analyse.entities.PaprikaClass;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 21:06
 * Description: A statistic on a class that can be processed using only the Paprika application model.
 */
public interface PaprikaClassStatistic {

    /**
     * Collects and creates the metric on a given class.
     *
     * @param paprikaClass the class to bind the metric to
     */
    void collectMetric(PaprikaClass paprikaClass);

}
