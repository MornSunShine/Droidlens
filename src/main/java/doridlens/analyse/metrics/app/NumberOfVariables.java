package doridlens.analyse.metrics.app;

import doridlens.analyse.entities.PaprikaApp;
import doridlens.analyse.metrics.UnaryMetric;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 20:48
 * Description: 变量数量统计
 */
public class NumberOfVariables extends UnaryMetric<Integer> {

    public static final String NAME = "number_of_variables";

    private NumberOfVariables(PaprikaApp paprikaApp, int value) {
        super(NAME, paprikaApp, value);
    }

    public static void createMetric(PaprikaApp paprikaApp, int value) {
        NumberOfVariables numberOfVariables = new NumberOfVariables(paprikaApp, value);
        numberOfVariables.updateEntity();
    }

}
