package doridlens.analyse.metrics.classes.condition;

import doridlens.analyse.entities.PaprikaClass;
import doridlens.analyse.metrics.Condition;
import soot.SootClass;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 20:49
 * Description:
 */
public abstract class ClassCondition extends Condition<SootClass, PaprikaClass> {

    public ClassCondition(String metricName) {
        super(metricName);
    }

}
