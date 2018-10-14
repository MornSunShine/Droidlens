package doridlens.analyse.metrics.classes.condition.counted.subclass;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 20:54
 * Description:
 */
public class IsAsyncTask extends IsSubClass {

    public static final String NAME = "is_async_task";
    public static final String NUMBER_METRIC = "number_of_async_tasks";
    public static final String ASYNC_ANDROID = "android.os.AsyncTask";

    public IsAsyncTask() {
        super(NAME, NUMBER_METRIC, ASYNC_ANDROID);
    }

}
