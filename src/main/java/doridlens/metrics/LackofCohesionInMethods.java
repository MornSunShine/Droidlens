package doridlens.metrics;

import doridlens.entity.PaprikaClass;

/**
 * Author: MaoMorn
 * Date: 2020/1/2
 * Time: 9:30
 * Description:
 */
public class LackofCohesionInMethods extends UnaryMetric<Integer> {

    private LackofCohesionInMethods(PaprikaClass paprikaClass) {
        this.value = paprikaClass.computeLCOM();
        this.entity = paprikaClass;
        this.name = "lack_of_cohesion_in_methods";
    }

    public static LackofCohesionInMethods createLackofCohesionInMethods(PaprikaClass paprikaClass) {
        LackofCohesionInMethods couplingBetweenObjects = new LackofCohesionInMethods(paprikaClass);
        couplingBetweenObjects.updateEntity();
        return couplingBetweenObjects;
    }
}
