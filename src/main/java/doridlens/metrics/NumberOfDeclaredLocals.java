package doridlens.metrics;

import doridlens.entity.PaprikaMethod;

/**
 * Author: MaoMorn
 * Date: 2020/1/2
 * Time: 9:34
 * Description:
 */
public class NumberOfDeclaredLocals extends UnaryMetric<Integer> {

    private NumberOfDeclaredLocals(PaprikaMethod paprikaMethod, int value) {
        this.value = value;
        this.entity = paprikaMethod;
        this.name = "number_of_declared_locals";
    }

    public static NumberOfDeclaredLocals createNumberOfDeclaredLocals(PaprikaMethod paprikaMethod, int value) {
        NumberOfDeclaredLocals numberOfDeclaredLocals = new NumberOfDeclaredLocals(paprikaMethod, value);
        numberOfDeclaredLocals.updateEntity();
        return  numberOfDeclaredLocals;
    }

}
