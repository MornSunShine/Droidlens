package doridlens.analyse.metrics.classes.condition.counted;

import soot.SootClass;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 20:51
 * Description:
 */
public class IsAbstractClass extends CountedClassCondition {

    public static final String NUMBER_METRIC = "number_of_abstract_classes";
    public static final String NAME = "is_abstract";

    public IsAbstractClass() {
        super(NAME, NUMBER_METRIC);
    }

    @Override
    public boolean matches(SootClass item) {
        return item.isAbstract();
    }
}

