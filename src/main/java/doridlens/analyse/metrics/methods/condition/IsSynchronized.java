package doridlens.analyse.metrics.methods.condition;

import soot.SootMethod;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 21:12
 * Description:
 */
public class IsSynchronized extends MethodCondition {

    public IsSynchronized() {
        super("is_synchronized");
    }

    @Override
    public boolean matches(SootMethod item) {
        return item.isSynchronized();
    }

}
