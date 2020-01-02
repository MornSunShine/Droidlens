package doridlens.metrics;

import doridlens.entity.PaprikaMethod;

/**
 * Author: MaoMorn
 * Date: 2020/1/2
 * Time: 9:23
 * Description:
 */
public class CyclomaticComplexity extends UnaryMetric<Integer> {

    private CyclomaticComplexity(PaprikaMethod paprikaMethod, int value) {
        this.value = value;
        this.entity = paprikaMethod;
        this.name = "cyclomatic_complexity";
    }

    public static CyclomaticComplexity createCyclomaticComplexity(PaprikaMethod paprikaMethod, int value) {
        CyclomaticComplexity cyclomaticComplexity =  new CyclomaticComplexity(paprikaMethod, value);
        cyclomaticComplexity.updateEntity();
        paprikaMethod.getPaprikaClass().addComplexity(value);
        return cyclomaticComplexity;
    }

}
