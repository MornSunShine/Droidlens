package doridlens.metrics;

import doridlens.entity.PaprikaMethod;

/**
 * Author: MaoMorn
 * Date: 2020/1/2
 * Time: 9:36
 * Description:
 */
public class NumberOfInstructions extends UnaryMetric<Integer> {

    private NumberOfInstructions(PaprikaMethod paprikaMethod, int value) {
        this.value = value;
        this.entity = paprikaMethod;
        this.name = "number_of_instructions";
    }

    public static NumberOfInstructions createNumberOfInstructions(PaprikaMethod paprikaMethod, int value) {
        NumberOfInstructions numberOfInstructions = new NumberOfInstructions(paprikaMethod, value);
        numberOfInstructions.updateEntity();
        return numberOfInstructions;
    }

}
