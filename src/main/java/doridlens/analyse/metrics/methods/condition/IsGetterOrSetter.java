package doridlens.analyse.metrics.methods.condition;

import doridlens.analyse.entities.PaprikaMethod;
import doridlens.analyse.entities.PaprikaVariable;
import doridlens.analyse.metrics.UnaryMetric;
import soot.SootMethod;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 21:11
 * Description:
 */
public class IsGetterOrSetter {

    public static final String IS_GETTER_NAME = "is_getter";
    public static final String IS_SETTER_NAME = "is_setter";

    public void createIfMatching(SootMethod sootMethod, PaprikaMethod paprikaMethod,
                                 boolean lastMethodHadSingleBranch) {
        if (lastMethodHadSingleBranch && paprikaMethod.getUsedVariables().size() == 1
                && sootMethod.getExceptions().size() == 0) {
            PaprikaVariable paprikaVariable = paprikaMethod.getUsedVariables().iterator().next();
            int parameterCount = sootMethod.getParameterCount();
            int unitSize = sootMethod.getActiveBody().getUnits().size();
            String returnType = paprikaMethod.getReturnType();
            if (parameterCount == 1 && unitSize <= 4 && returnType.equals("void")) {
                createMetric(IS_SETTER_NAME, paprikaMethod);
            } else if (parameterCount == 0 && unitSize <= 3 && returnType.equals(paprikaVariable.getType())) {
                createMetric(IS_GETTER_NAME, paprikaMethod);
            }
        }
    }

    private void createMetric(String name, PaprikaMethod paprikaMethod) {
        UnaryMetric<Boolean> metric = new UnaryMetric<>(name, paprikaMethod, true);
        metric.updateEntity();
    }


}

