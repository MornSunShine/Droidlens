package doridlens.analyse.metrics.methods.stat;

import doridlens.analyse.entities.PaprikaMethod;
import doridlens.analyse.metrics.UnaryMetric;
import soot.SootMethod;
import soot.Unit;
import soot.grimp.internal.GLookupSwitchStmt;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 21:13
 * Description:
 */
public class CyclomaticComplexity implements MethodStatistic {

    public static final String NAME = "cyclomatic_complexity";

    private int lastMethodBranches;

    @Override
    public void collectMetric(SootMethod sootMethod, PaprikaMethod paprikaMethod) {
        int complexity = getCyclomaticComplexity(sootMethod);
        UnaryMetric<Integer> metric = new UnaryMetric<>(NAME, paprikaMethod,
                complexity);
        metric.updateEntity();
        paprikaMethod.getPaprikaClass().addComplexity(complexity);
    }

    private int getCyclomaticComplexity(SootMethod sootMethod) {
        int nbOfBranches = 1;
        for (Unit sootUnit :sootMethod.getActiveBody().getUnits()) {
            // Cyclomatic complexity
            if (sootUnit.branches()) {
                if (sootUnit.fallsThrough()) nbOfBranches++;
                else if (sootUnit instanceof GLookupSwitchStmt)
                    nbOfBranches += ((GLookupSwitchStmt) sootUnit).getLookupValues().size();
            }
        }
        this.lastMethodBranches = nbOfBranches;
        return nbOfBranches;
    }

    public boolean lastMethodHadASingleBranch() {
        return lastMethodBranches == 1;
    }

}

