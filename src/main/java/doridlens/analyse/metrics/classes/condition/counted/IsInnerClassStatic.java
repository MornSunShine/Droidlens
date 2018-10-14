package doridlens.analyse.metrics.classes.condition.counted;

import doridlens.analyse.entities.PaprikaClass;
import doridlens.analyse.metrics.common.IsStatic;
import soot.SootClass;
import soot.SootField;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 20:51
 * Description:
 */
public class IsInnerClassStatic extends CountedClassCondition {

    public static final String NAME = "is_inner_class";
    public static final String NUMBER_METRIC = "number_of_inner_classes";

    public IsInnerClassStatic() {
        super(NAME, NUMBER_METRIC);
    }

    @Override
    public boolean matches(SootClass item) {
        return item.isInnerClass();
    }

    @Override
    public boolean createIfMatching(SootClass item, PaprikaClass paprikaClass) {
        boolean match = super.createIfMatching(item, paprikaClass);
        if (match && item != null && isInnerClassStatic(item)) {
            IsStatic.createIsStatic(paprikaClass);
        }
        return match;
    }

    /**
     * Fix to determine if a class is static or not
     */
    private boolean isInnerClassStatic(SootClass innerClass) {
        for (SootField sootField : innerClass.getFields()) {
            // we are looking if the field for non static inner class generated during the compilation (with the convention name) exists
            if (sootField.getName().equals("this$0")) {
                // in this case we can return false
                return false;
            }
        }
        return true;
    }

}
