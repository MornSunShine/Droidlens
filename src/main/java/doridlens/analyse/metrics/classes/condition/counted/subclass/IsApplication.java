package doridlens.analyse.metrics.classes.condition.counted.subclass;

import doridlens.analyse.entities.PaprikaApp;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 20:53
 * Description:
 */
public class IsApplication extends IsSubClass {

    public static final String NAME = "is_application";

    public IsApplication() {
        super(NAME, "NO-OP", "android.app.Application");
    }

    @Override
    public void createNumberMetric(PaprikaApp app) {
        // Do nothing
    }

}
