package doridlens.metrics;

import doridlens.entity.PaprikaApp;

/**
 * Author: MaoMorn
 * Date: 2020/1/2
 * Time: 9:32
 * Description:
 */
public class NumberOfAsyncTasks extends UnaryMetric<Integer> {

    private NumberOfAsyncTasks(PaprikaApp paprikaApp, int value) {
        this.value = value;
        this.entity = paprikaApp;
        this.name = "number_of_async_tasks";
    }

    public static NumberOfAsyncTasks createNumberOfAsyncTasks(PaprikaApp paprikaApp, int value) {
        NumberOfAsyncTasks numberOfAsyncTasks = new NumberOfAsyncTasks(paprikaApp, value);
        numberOfAsyncTasks.updateEntity();
        return numberOfAsyncTasks;
    }

}
