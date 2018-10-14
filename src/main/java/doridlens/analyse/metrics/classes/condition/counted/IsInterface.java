package doridlens.analyse.metrics.classes.condition.counted;

import soot.SootClass;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 20:52
 * Description:
 */
public class IsInterface extends CountedClassCondition {

    public static final String NAME = "is_interface";
    public static final String NUMBER_METRIC = "number_of_interfaces";

    public IsInterface() {
        super(NAME, NUMBER_METRIC);
    }

    @Override
    public boolean matches(SootClass item) {
        return item.isInterface();
    }

}
