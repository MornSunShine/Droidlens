package doridlens.analyse.metrics.common;

import doridlens.analyse.entities.Entity;
import doridlens.analyse.entities.PaprikaClass;
import doridlens.analyse.entities.PaprikaMethod;
import doridlens.analyse.entities.PaprikaVariable;
import doridlens.analyse.metrics.UnaryMetric;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 21:08
 * Description: A condition that can be checked on multiple Soot objects, and applied to
 *              the matching Paprika entities.
 */
@SuppressWarnings("UnusedReturnValue")
public abstract class CommonCondition {

    private String metricName;

    public CommonCondition(String metricName) {
        this.metricName = metricName;
    }

    public boolean matches(SootClass sootClass) {
        throw new UnsupportedOperationException();
    }

    public boolean matches(SootField sootField) {
        throw new UnsupportedOperationException();
    }

    public boolean matches(SootMethod sootMethod) {
        throw new UnsupportedOperationException();
    }

    public boolean createIfMatching(SootClass sootClass, PaprikaClass paprikaClass) {
        return createMetric(matches(sootClass), paprikaClass);
    }

    public boolean createIfMatching(SootMethod sootMethod, PaprikaMethod paprikaMethod) {
        return createMetric(matches(sootMethod), paprikaMethod);
    }

    public boolean createIfMatching(SootField sootField, PaprikaVariable paprikaVariable) {
        return createMetric(matches(sootField), paprikaVariable);
    }

    public boolean createMetric(boolean match, Entity entity) {
        if (match) {
            UnaryMetric<Boolean> metric = new UnaryMetric<>(metricName, entity, true);
            metric.updateEntity();
            return true;
        }
        return false;
    }

}

