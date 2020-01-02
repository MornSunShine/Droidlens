package doridlens.metrics;

import doridlens.entity.PaprikaClass;

/**
 * Author: MaoMorn
 * Date: 2020/1/2
 * Time: 9:19
 * Description:
 */
public class ClassComplexity extends UnaryMetric<Integer> {

    private ClassComplexity(PaprikaClass paprikaClass) {
        this.value = paprikaClass.getComplexity();
        this.entity = paprikaClass;
        this.name = "class_complexity";
    }

    public static ClassComplexity createClassComplexity(PaprikaClass paprikaClass) {
        ClassComplexity classComplexity = new ClassComplexity(paprikaClass);
        classComplexity.updateEntity();
        return classComplexity;
    }
}
