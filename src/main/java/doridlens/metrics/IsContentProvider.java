package doridlens.metrics;

import doridlens.entity.PaprikaClass;

/**
 * Author: MaoMorn
 * Date: 2020/1/2
 * Time: 9:27
 * Description:
 */
public class IsContentProvider extends UnaryMetric<Boolean> {

    private IsContentProvider(PaprikaClass entity, boolean value) {
        this.value = value;
        this.entity = entity;
        this.name = "is_content_provider";
    }

    public static IsContentProvider createIsContentProvider(PaprikaClass entity, boolean value) {
        IsContentProvider isContentProvider= new IsContentProvider(entity, value);
        isContentProvider.updateEntity();
        return isContentProvider;
    }
}
