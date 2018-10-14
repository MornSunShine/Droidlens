package doridlens.analyse.metrics.classes.condition.counted.subclass;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 20:55
 * Description:
 */
public class IsService extends IsSubClass {

    public static final String NAME = "is_service";
    public static final String NUMBER_METRIC = "number_of_services";

    public IsService() {
        super(NAME, NUMBER_METRIC, "android.app.Service");
    }

}
