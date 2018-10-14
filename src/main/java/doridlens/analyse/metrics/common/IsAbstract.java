package doridlens.analyse.metrics.common;

import soot.SootClass;
import soot.SootMethod;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 21:09
 * Description:
 */
public class IsAbstract extends CommonCondition {

    public IsAbstract() {
        super("is_abstract");
    }

    @Override
    public boolean matches(SootClass sootClass) {
        return sootClass.isAbstract();
    }

    @Override
    public boolean matches(SootMethod sootMethod) {
        return sootMethod.isAbstract();
    }

}
