package doridlens.analyse.metrics.classes.stat.soot;

import doridlens.analyse.entities.PaprikaClass;
import soot.SootClass;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 21:08
 * Description: A statistic on a class that needs both the Soot and paprika application models
 *              to be processed.
 */
public interface SootClassStatistic {

    /**
     * Collects and creates the metric on a given class.
     *
     * @param sootClass    the Soot representation of the class
     * @param paprikaClass the Paprika representation of the class
     */
    void collectMetric(SootClass sootClass, PaprikaClass paprikaClass);

}
