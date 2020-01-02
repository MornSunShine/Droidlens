package doridlens.metrics;

import doridlens.entity.PaprikaClass;

/**
 * Author: MaoMorn
 * Date: 2020/1/2
 * Time: 9:29
 * Description:
 */
public class IsService extends UnaryMetric<Boolean> {

    private IsService(PaprikaClass entity, boolean value) {
        this.value = value;
        this.entity = entity;
        this.name = "is_service";
    }

    public static IsService createIsService(PaprikaClass entity, boolean value) {
        IsService isService= new IsService(entity, value);
        isService.updateEntity();
        return isService;
    }
}
