package doridlens.analyse.metrics;

import doridlens.analyse.entities.Entity;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 20:42
 * Description: A metric stored in a single entity.
 */
public class UnaryMetric<E> extends Metric<E> {

    protected Entity entity;

    public UnaryMetric(String name, Entity entity, E value) {
        super(name, value);
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    protected void setEntity(Entity entity) {
        this.entity = entity;
    }

    public String toString() {
        return this.entity + " " + this.name + " : " + this.value;
    }

    public void updateEntity() {
        entity.addMetric(this);
    }

}
