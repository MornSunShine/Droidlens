package doridlens.analyse.metrics;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 20:37
 * Description: A boolean condition linked to a metric.
 */

import doridlens.analyse.entities.Entity;

/**
 * @param <T>
 * @param <E>
 */
public abstract class Condition<T, E extends Entity> {

    private String metricName;

    /**
     * Constructor.
     *
     * @param metricName the name of the metric
     */
    public Condition(String metricName) {
        this.metricName = metricName;
    }

    /**
     * Checks whether the given item matches the metric condition or not.
     *
     * @param item the item to check
     * @return true if the metric must be created, false otherwise
     */
    public abstract boolean matches(T item);

    /**
     * Creates the metric if the given object matches this Condition.
     *
     * @param item   the item to check
     * @param entity the entity to bind the metric to if the condition matches
     * @return true if the metric was created, false otherwise
     */
    public boolean createIfMatching(T item, E entity) {
        if (matches(item)) {
            UnaryMetric<Boolean> metric = new UnaryMetric<>(metricName, entity, true);
            metric.updateEntity();
            return true;
        }
        return false;
    }
}
