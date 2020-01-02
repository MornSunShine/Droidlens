package doridlens.metrics;

import doridlens.entity.PaprikaClass;

/**
 * Author: MaoMorn
 * Date: 2020/1/2
 * Time: 9:33
 * Description:
 */
public class NumberOfChildren extends UnaryMetric<Integer> {

    private NumberOfChildren(PaprikaClass paprikaClass) {
        this.value = paprikaClass.getChildren();
        this.entity = paprikaClass;
        this.name = "number_of_children";
    }

    public static NumberOfChildren createNumberOfChildren(PaprikaClass paprikaClass) {
        NumberOfChildren numberOfChildren = new NumberOfChildren(paprikaClass);
        numberOfChildren.updateEntity();
        return numberOfChildren;
    }
}
