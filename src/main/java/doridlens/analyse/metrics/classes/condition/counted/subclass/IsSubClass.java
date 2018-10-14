package doridlens.analyse.metrics.classes.condition.counted.subclass;

import doridlens.analyse.metrics.classes.condition.counted.CountedClassCondition;
import soot.SootClass;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 20:56
 * Description: Metric used to count subclasses of specific Android classes: Activities, Views...
 */
public abstract class IsSubClass extends CountedClassCondition {

    private String androidClass;

    /**
     * 构造函数
     *
     * @param conditionMetric the name of the metric created on a PaprikaClass
     * @param numberMetric    the name of the count metric created on a PaprikaApp
     * @param androidClass    the name of the Android class to check
     */
    public IsSubClass(String conditionMetric, String numberMetric, String androidClass) {
        super(conditionMetric, numberMetric);
        this.androidClass = androidClass;
    }

    @Override
    public boolean matches(SootClass item) {
        return isSubClass(item, androidClass);
    }

    protected boolean isSubClass(SootClass sootClass, String className) {
        do {
            if (sootClass.getName().equals(className)) return true;
            sootClass = sootClass.getSuperclass();
        } while (sootClass.hasSuperclass());
        return false;
    }

}

