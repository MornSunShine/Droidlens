package doridlens.analyse.metrics.app;

import doridlens.analyse.entities.PaprikaApp;
import doridlens.analyse.metrics.UnaryMetric;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 20:47
 * Description: Debuggable Release Smell判断
 */
public class IsDebuggableRelease extends UnaryMetric<Boolean> {

    public static final String NAME = "is_debuggable_release";

    private IsDebuggableRelease(PaprikaApp app) {
        super(NAME, app, true);
    }

    public static void createMetric(PaprikaApp app) {
        IsDebuggableRelease metric = new IsDebuggableRelease(app);
        metric.updateEntity();
    }

}