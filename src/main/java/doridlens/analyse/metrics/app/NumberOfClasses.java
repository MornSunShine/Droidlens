package doridlens.analyse.metrics.app;

import doridlens.analyse.entities.PaprikaApp;
import doridlens.analyse.metrics.UnaryMetric;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 20:48
 * Description: 类数量统计
 */
public class NumberOfClasses extends UnaryMetric<Integer> {

    public static final String NAME = "number_of_classes";

    private NumberOfClasses(PaprikaApp paprikaApp, int value) {
        super(NAME, paprikaApp, value);
    }

    public static void createMetric(PaprikaApp paprikaApp, int value) {
        NumberOfClasses numberOfClasses = new NumberOfClasses(paprikaApp, value);
        numberOfClasses.updateEntity();
    }

}
