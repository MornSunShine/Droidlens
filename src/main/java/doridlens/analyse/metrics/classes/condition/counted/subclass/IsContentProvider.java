package doridlens.analyse.metrics.classes.condition.counted.subclass;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 20:55
 * Description:
 */
public class IsContentProvider extends IsSubClass {

    public static final String NUMBER_METRIC = "number_of_content_providers";
    public static final String NAME = "is_content_provider";

    public IsContentProvider() {
        super(NAME, NUMBER_METRIC, "android.content.ContentProvider");
    }

}