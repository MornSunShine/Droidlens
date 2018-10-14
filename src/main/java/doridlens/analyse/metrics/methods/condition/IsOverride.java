package doridlens.analyse.metrics.methods.condition;

import soot.SootClass;
import soot.SootMethod;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 21:12
 * Description:
 */
public class IsOverride extends MethodCondition {

    public static final String NAME = "is_override";

    public IsOverride() {
        super(NAME);
    }

    @Override
    public boolean matches(SootMethod item) {
        SootClass sootClass = item.getDeclaringClass();
        for (SootClass inter : sootClass.getInterfaces()) {
            if (classContainsMethod(inter, item)) return true;
            while (inter.hasSuperclass()) {
                inter = inter.getSuperclass();
                if (classContainsMethod(inter, item)) return true;
            }
        }
        while (sootClass.hasSuperclass()) {
            sootClass = sootClass.getSuperclass();
            if (classContainsMethod(sootClass, item)) return true;
        }
        return false;
    }


    /**
     * Test if a class contains a method with same name, parameters and return type
     */
    private boolean classContainsMethod(SootClass sootClass, SootMethod sootMethod) {
        // Here unsafe just means it will return null (instead of throwing an exception)
        return sootClass.getMethodUnsafe(sootMethod.getName(),
                sootMethod.getParameterTypes(), sootMethod.getReturnType()) != null;
    }

}

