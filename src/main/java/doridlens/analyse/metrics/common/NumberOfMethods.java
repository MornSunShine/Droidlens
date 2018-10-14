package doridlens.analyse.metrics.common;

import doridlens.analyse.entities.Entity;
import doridlens.analyse.entities.PaprikaApp;
import doridlens.analyse.entities.PaprikaClass;
import doridlens.analyse.metrics.UnaryMetric;
import doridlens.analyse.metrics.classes.stat.soot.SootClassStatistic;
import soot.SootClass;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 21:10
 * Description: Can be applied to a class or an application.
 */
public class NumberOfMethods implements SootClassStatistic {

    public static final String NAME = "number_of_methods";

    public static void createNumberOfMethods(PaprikaApp app, int value) {
        createMetric(app, value);
    }

    private static void createMetric(Entity entity, int value) {
        UnaryMetric<Integer> metric = new UnaryMetric<>(NAME, entity, value);
        metric.updateEntity();
    }

    @Override
    public void collectMetric(SootClass sootClass, PaprikaClass paprikaClass) {
        createMetric(paprikaClass, sootClass.getMethodCount());
    }

}

