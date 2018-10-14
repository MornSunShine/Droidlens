package doridlens.analyse.entities;

import doridlens.analyse.metrics.Metric;
import doridlens.analyse.metrics.UnaryMetric;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 20:28
 * Description: 抽象实体类,方便工具中涉及的实体类封装
 */
public abstract class Entity {

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
