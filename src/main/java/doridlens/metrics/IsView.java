package doridlens.metrics;

import doridlens.entity.PaprikaClass;

/**
 * Author: MaoMorn
 * Date: 2020/1/2
 * Time: 9:30
 * Description:
 */
public class IsView extends UnaryMetric<Boolean> {

    private IsView(PaprikaClass entity, boolean value) {
        this.value = value;
        this.entity = entity;
        this.name = "is_view";
    }

    public static IsView createIsView(PaprikaClass entity, boolean value) {
        IsView isView= new IsView(entity, value);
        isView.updateEntity();
        return isView;
    }
}
