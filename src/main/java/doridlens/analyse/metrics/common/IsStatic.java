package doridlens.analyse.metrics.common;

import doridlens.analyse.entities.PaprikaClass;
import doridlens.analyse.metrics.UnaryMetric;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 21:10
 * Description:
 */
public class IsStatic extends CommonCondition {

    public static final String NAME = "is_static";

    public IsStatic() {
        super(NAME);
    }

    public static void createIsStatic(PaprikaClass paprikaClass) {
        UnaryMetric<Boolean> metric = new UnaryMetric<>(NAME, paprikaClass, true);
        metric.updateEntity();
    }

    @Override
    public boolean matches(SootClass sootClass) {
        throw new UnsupportedOperationException("SootClass.isStatic() does not behave as expected, " +
                "perform your own custom check instead");
    }

    @Override
    public boolean matches(SootMethod sootMethod) {
        return sootMethod.isStatic();
    }

    @Override
    public boolean matches(SootField sootField) {
        return sootField.isStatic();
    }

}

