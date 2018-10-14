package doridlens.analyse.analyzer;

import doridlens.analyse.entities.PaprikaArgument;
import doridlens.analyse.entities.PaprikaMethod;
import doridlens.analyse.entities.PaprikaVariable;
import doridlens.analyse.metrics.methods.condition.*;
import doridlens.analyse.metrics.methods.stat.CyclomaticComplexity;
import doridlens.analyse.metrics.methods.stat.MethodStatistic;
import doridlens.analyse.metrics.methods.stat.NumberOfDeclaredLocals;
import doridlens.analyse.metrics.methods.stat.NumberOfInstructions;
import soot.*;
import soot.jimple.FieldRef;

import java.util.List;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 20:24
 * Description: Processes and collect metrics for methods having an active body.
 */
public class MethodBodyProcessor {

    private CyclomaticComplexity complexity = new CyclomaticComplexity();

    private MethodStatistic[] statistics = {
            new NumberOfDeclaredLocals(),
            new NumberOfInstructions(),
            complexity
    };

    private MethodCondition isInit;
    private MethodCondition isOverride;
    private IsGetterOrSetter isGetterOrSetter;
    private MethodCondition usesPublicData;

    public MethodBodyProcessor() {
        this.usesPublicData = new UsesPublicData();
        this.isInit = new IsInit();
        this.isGetterOrSetter = new IsGetterOrSetter();
        this.isOverride = new IsOverride();
    }

    /**
     * Collect statistics and add method metrics to the application model if they apply.
     * Registers method arguments in the application model.
     * Registers which fields are used in the methods, which is necessary to compute LCOM2.
     *
     * @param sootMethod    the Soot representation of the method
     * @param paprikaMethod the Paprika representation of the model
     */
    public void processMethodBody(SootMethod sootMethod, PaprikaMethod paprikaMethod) {
        registerArgs(sootMethod, paprikaMethod);
        for (MethodStatistic stat : statistics) {
            stat.collectMetric(sootMethod, paprikaMethod);
        }
        computeLackOfCohesion(sootMethod, paprikaMethod);
        if (!isInit.createIfMatching(sootMethod, paprikaMethod)) {
            isOverride.createIfMatching(sootMethod, paprikaMethod);
            isGetterOrSetter.createIfMatching(sootMethod, paprikaMethod, complexity.lastMethodHadASingleBranch());
        }
        usesPublicData.createIfMatching(sootMethod, paprikaMethod);
    }

    private void registerArgs(SootMethod sootMethod, PaprikaMethod paprikaMethod) {
        int i = 0;
        for (Type type : sootMethod.getParameterTypes()) {
            i++;
            PaprikaArgument.create(type.toString(), i, paprikaMethod);
        }
    }

    private void computeLackOfCohesion(SootMethod sootMethod, PaprikaMethod paprikaMethod) {
        for (Unit sootUnit : sootMethod.getActiveBody().getUnits()) {
            List<ValueBox> boxes = sootUnit.getUseAndDefBoxes();
            for (ValueBox valueBox : boxes) {
                Value value = valueBox.getValue();
                if (value instanceof FieldRef) {
                    SootFieldRef field = ((FieldRef) value).getFieldRef();
                    if (field.declaringClass() == sootMethod.getDeclaringClass()) {
                        PaprikaVariable paprikaVariable = paprikaMethod.getPaprikaClass().findVariable(field.name());
                        // If we don't find the field it's inherited and thus not used for LCOM2
                        if (paprikaVariable != null) {
                            paprikaMethod.useVariable(paprikaVariable);
                        }
                    }
                }
            }
        }
    }

}
