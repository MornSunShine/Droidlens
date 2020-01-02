package doridlens.metrics;

import doridlens.entity.PaprikaClass;

/**
 * Author: MaoMorn
 * Date: 2020/1/2
 * Time: 9:31
 * Description:
 */
public class NPathComplexity extends UnaryMetric<Integer> {

    private NPathComplexity(PaprikaClass paprikaClass) {
        this.value = paprikaClass.computeNPathComplexity();
        this.entity = paprikaClass;
        this.name = "npath_complexity";
    }

    public static NPathComplexity createNPathComplexity(PaprikaClass paprikaClass) {
        NPathComplexity npath_complexity = new NPathComplexity(paprikaClass);
        npath_complexity.updateEntity();
        return npath_complexity;
    }

}
