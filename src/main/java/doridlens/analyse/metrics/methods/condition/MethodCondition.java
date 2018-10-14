package doridlens.analyse.metrics.methods.condition;

import doridlens.analyse.entities.PaprikaMethod;
import doridlens.analyse.metrics.Condition;
import soot.SootMethod;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 21:13
 * Description:
 */
public abstract class MethodCondition extends Condition<SootMethod, PaprikaMethod> {

    public MethodCondition(String metricName) {
        super(metricName);
    }

}
