package doridlens.analyse.metrics.common;

import soot.SootClass;
import soot.SootField;
import soot.SootMethod;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 21:09
 * Description:
 */
public class IsFinal extends CommonCondition{

    public IsFinal() {
        super("is_final");
    }

    @Override
    public boolean matches(SootClass sootClass) {
        return sootClass.isFinal();
    }

    @Override
    public boolean matches(SootField sootField) {
        return sootField.isFinal();
    }

    @Override
    public boolean matches(SootMethod sootMethod) {
        return sootMethod.isFinal();
    }

}
