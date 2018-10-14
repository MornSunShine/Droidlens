package doridlens.analyse.metrics.classes.condition.counted.subclass;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 20:56
 * Description:
 */
public class IsView extends IsSubClass {

    public static final String NAME = "is_view";
    public static final String NUMBER_METRIC = "number_of_views";
    public static final String ANDROID_VIEW = "android.view.View";

    public IsView() {
        super(NAME, NUMBER_METRIC, ANDROID_VIEW);
    }

}
