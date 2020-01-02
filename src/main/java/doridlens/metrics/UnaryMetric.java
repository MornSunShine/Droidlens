package doridlens.metrics;

import doridlens.entity.Entity;

/**
 * Author: MaoMorn
 * Date: 2020/1/2
 * Time: 10:08
 * Description:
 */
public abstract class UnaryMetric<E> extends Metric {
    protected Entity entity;

    public Entity getEntity() {
        return entity;
    }

    protected void setEntity(Entity entity) {
        this.entity = entity;
    }

    public String toString() {
        return this.entity + " " + this.name + " : " + this.value;
    }

    protected void updateEntity() {
        entity.addMetric(this);
    }
}
