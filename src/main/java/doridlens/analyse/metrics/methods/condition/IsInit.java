package doridlens.analyse.metrics.methods.condition;

import soot.SootMethod;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 21:12
 * Description:
 */
public class IsInit extends MethodCondition {

    public static final String NAME = "is_init";

    public IsInit() {
        super(NAME);
    }

    @Override
    public boolean matches(SootMethod item) {
        String name = item.getName();
        return name.equals("<prepareSoot>") || name.equals("<clinit>");
    }
}

