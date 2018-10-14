package doridlens.analyse.metrics.classes.condition.counted.subclass;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 20:52
 * Description:
 */
public class IsActivity extends IsSubClass {

    public static final String NAME  = "is_activity";
    public static final String NUMBER_METRIC = "number_of_activities";

    public IsActivity() {
        super(NAME, NUMBER_METRIC, "android.app.Activity");
    }

}

