package doridlens.metrics;

import doridlens.entity.PaprikaClass;

/**
 * Author: MaoMorn
 * Date: 2020/1/2
 * Time: 9:26
 * Description:
 */
public class IsBitmap extends UnaryMetric<Boolean> {

    private IsBitmap(PaprikaClass paprikaClass, boolean value){
        this.value = value;
        this.entity = paprikaClass;
        this.name = "is_bitmap";
    }

    public static IsBitmap createIsBitmap(PaprikaClass paprikaClass, boolean value) {
        IsBitmap isBitmap = new IsBitmap(paprikaClass, value);
        isBitmap.updateEntity();
        return isBitmap;
    }

}
