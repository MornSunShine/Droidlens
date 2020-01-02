package doridlens.metrics;

import doridlens.entity.PaprikaApp;

/**
 * Author: MaoMorn
 * Date: 2020/1/2
 * Time: 9:33
 * Description:
 */
public class NumberOfBroadcastReceivers extends UnaryMetric<Integer> {

    private NumberOfBroadcastReceivers(PaprikaApp paprikaApp, int value) {
        this.value = value;
        this.entity = paprikaApp;
        this.name = "number_of_broadcast_receivers";
    }

    public static NumberOfBroadcastReceivers createNumberOfBroadcastReceivers(PaprikaApp paprikaApp, int value) {
        NumberOfBroadcastReceivers numberOfBroadcastReceivers =  new NumberOfBroadcastReceivers(paprikaApp, value);
        numberOfBroadcastReceivers.updateEntity();
        return numberOfBroadcastReceivers;
    }

}
