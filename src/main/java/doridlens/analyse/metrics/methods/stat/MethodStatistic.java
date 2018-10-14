package doridlens.analyse.metrics.methods.stat;

import doridlens.analyse.entities.PaprikaMethod;
import soot.SootMethod;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 21:13
 * Description:
 */
public interface MethodStatistic {

    void collectMetric(SootMethod sootMethod, PaprikaMethod paprikaMethod);

}
