package doridlens.metrics;

import doridlens.entity.PaprikaClass;

/**
 * Author: MaoMorn
 * Date: 2020/1/2
 * Time: 9:24
 * Description:
 */
public class DepthOfInheritance extends UnaryMetric<Integer> {
    private DepthOfInheritance(PaprikaClass paprikaClass, int value) {
        this.value = value;
        this.entity = paprikaClass;
        this.name = "depth_of_inheritance";
    }

    public static DepthOfInheritance createDepthOfInheritance(PaprikaClass paprikaClass, int value) {
        DepthOfInheritance depthOfInheritance = new DepthOfInheritance(paprikaClass, value);
        depthOfInheritance.updateEntity();
        return depthOfInheritance;
    }

}
