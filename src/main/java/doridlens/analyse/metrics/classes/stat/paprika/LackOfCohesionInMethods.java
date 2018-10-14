package doridlens.analyse.metrics.classes.stat.paprika;

import doridlens.analyse.entities.PaprikaClass;
import doridlens.analyse.entities.PaprikaMethod;
import doridlens.analyse.metrics.UnaryMetric;

import java.util.Set;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 21:05
 * Description: LCOM2 (Chidamber et Kemerer 1991)
 */
public class LackOfCohesionInMethods implements PaprikaClassStatistic {

    public static final String NAME = "lack_of_cohesion_in_methods";

    @Override
    public void collectMetric(PaprikaClass paprikaClass) {
        UnaryMetric<Integer> metric = new UnaryMetric<>(NAME, paprikaClass,
                computeLCOM(paprikaClass));
        metric.updateEntity();
    }

    private int computeLCOM(PaprikaClass paprikaClass) {
        Set<PaprikaMethod> methodSet = paprikaClass.getPaprikaMethods();
        PaprikaMethod[] methods = methodSet.toArray(new PaprikaMethod[0]);
        int methodCount = methods.length;
        int haveFieldInCommon = 0;
        int noFieldInCommon = 0;
        for (int i = 0; i < methodCount; i++) {
            for (int j = i + 1; j < methodCount; j++) {
                if (methods[i].haveCommonFields(methods[j])) {
                    haveFieldInCommon++;
                } else {
                    noFieldInCommon++;
                }
            }
        }
        int lcom = noFieldInCommon - haveFieldInCommon;
        return lcom > 0 ? lcom : 0;
    }

}

