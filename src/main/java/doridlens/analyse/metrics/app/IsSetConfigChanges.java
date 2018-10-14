package doridlens.analyse.metrics.app;

import doridlens.analyse.entities.PaprikaApp;
import doridlens.analyse.metrics.UnaryMetric;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 20:47
 * Description:
 */
public class IsSetConfigChanges extends UnaryMetric {
    public static final String NAME = "is_set_config_changes";

    private IsSetConfigChanges(PaprikaApp app) {
        super(NAME, app, true);
    }

    public static void createMetric(PaprikaApp app) {
        IsSetConfigChanges metric = new IsSetConfigChanges(app);
        metric.updateEntity();
    }
}
