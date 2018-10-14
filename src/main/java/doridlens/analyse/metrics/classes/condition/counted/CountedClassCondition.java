package doridlens.analyse.metrics.classes.condition.counted;

import doridlens.analyse.entities.PaprikaApp;
import doridlens.analyse.entities.PaprikaClass;
import doridlens.analyse.metrics.UnaryMetric;
import doridlens.analyse.metrics.classes.condition.ClassCondition;
import soot.SootClass;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 20:50
 * Description: A class condition that also includes a metric on the Paprika application to count the number
 *              of times it was created.
 */
public abstract class CountedClassCondition extends ClassCondition {

    private String numberMetric;
    private int count = 0;

    /**
     * Constructor.
     *
     * @param conditionMetric the name of the metric created on a PaprikaClass
     * @param numberMetric    the name of the count metric created on a PaprikaApp
     */
    public CountedClassCondition(String conditionMetric, String numberMetric) {
        super(conditionMetric);
        this.numberMetric = numberMetric;
    }

    @Override
    public boolean createIfMatching(SootClass item, PaprikaClass paprikaClass) {
        if (super.createIfMatching(item, paprikaClass)) {
            count++;
            return true;
        }
        return false;
    }

    /**
     * Creates the count metric on the given application.
     *
     * @param app the app to bind the count metric to
     */
    public void createNumberMetric(PaprikaApp app) {
        UnaryMetric<Integer> metric = new UnaryMetric<>(numberMetric, app, count);
        metric.updateEntity();
    }

}
