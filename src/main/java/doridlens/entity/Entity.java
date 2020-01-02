package doridlens.entity;

import doridlens.metrics.Metric;
import doridlens.metrics.UnaryMetric;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: MaoMorn
 * Date: 2020/1/2
 * Time: 9:17
 * Description:
 */
public class Entity {
    protected String name;
    protected List<Metric> metrics = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Metric> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<Metric> metrics) {
        this.metrics = metrics;
    }

    public void addMetric(UnaryMetric unaryMetric) {
        this.metrics.add(unaryMetric);
    }

    @Override
    public String toString() {
        return name;
    }
}
