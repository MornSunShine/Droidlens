package doridlens.metrics;

import doridlens.entity.PaprikaApp;

/**
 * Author: MaoMorn
 * Date: 2020/1/2
 * Time: 9:38
 * Description:
 */
public class NumberOfViews extends UnaryMetric<Integer> {

    private NumberOfViews(PaprikaApp paprikaApp, int value) {
        this.value = value;
        this.entity = paprikaApp;
        this.name = "number_of_views";
    }

    public static NumberOfViews createNumberOfViews(PaprikaApp paprikaApp, int value) {
        NumberOfViews numberOfViews = new NumberOfViews(paprikaApp, value);
        numberOfViews.updateEntity();
        return numberOfViews;
    }

}
