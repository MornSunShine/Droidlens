package doridlens.metrics;

import doridlens.entity.PaprikaClass;

/**
 * Author: MaoMorn
 * Date: 2020/1/2
 * Time: 9:25
 * Description:
 */
public class IsActivity extends UnaryMetric<Boolean> {

    private IsActivity(PaprikaClass entity, boolean value) {
        this.value = value;
        this.entity = entity;
        this.name = "is_activity";
    }

    public static IsActivity createIsActivity(PaprikaClass entity, boolean value) {
        IsActivity isActivity= new IsActivity(entity, value);
        isActivity.updateEntity();
        return isActivity;
    }
}
