package doridlens.metrics;

import doridlens.entity.PaprikaApp;

/**
 * Author: MaoMorn
 * Date: 2020/1/2
 * Time: 9:34
 * Description:
 */
public class NumberOfContentProviders extends UnaryMetric<Integer> {

    private NumberOfContentProviders(PaprikaApp paprikaApp, int value) {
        this.value = value;
        this.entity = paprikaApp;
        this.name = "number_of_content_providers";
    }

    public static NumberOfContentProviders createNumberOfContentProviders(PaprikaApp paprikaApp, int value) {
        NumberOfContentProviders numberOfContentProviders = new NumberOfContentProviders(paprikaApp, value);
        numberOfContentProviders.updateEntity();
        return numberOfContentProviders;
    }

}
