package doridlens.metrics;

import doridlens.entity.PaprikaClass;

/**
 * Author: MaoMorn
 * Date: 2020/1/2
 * Time: 9:19
 * Description:
 */
public class CouplingBetweenObjects extends UnaryMetric<Integer> {

    private CouplingBetweenObjects(PaprikaClass paprikaClass) {
        this.value = paprikaClass.getCouplingValue();
        this.entity = paprikaClass;
        this.name = "coupling_between_object_classes";
    }

    public static CouplingBetweenObjects createCouplingBetweenObjects(PaprikaClass paprikaClass) {
        CouplingBetweenObjects couplingBetweenObjects = new CouplingBetweenObjects(paprikaClass);
        couplingBetweenObjects.updateEntity();
        return couplingBetweenObjects;
    }
}
