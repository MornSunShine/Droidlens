package doridlens.analyse.metrics.classes.condition.counted.subclass;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 20:55
 * Description:
 */
public class IsBroadcastReceiver extends IsSubClass {

    public static final String NAME = "is_broadcast_receiver";
    public static final String NUMBER_METRIC = "number_of_broadcast_receivers";

    public IsBroadcastReceiver() {
        super(NAME, NUMBER_METRIC, "android.content.BroadcastReceiver");
    }

}
