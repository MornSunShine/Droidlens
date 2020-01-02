package doridlens.metrics;

import doridlens.entity.Entity;

/**
 * Author: MaoMorn
 * Date: 2020/1/2
 * Time: 9:18
 * Description:
 */
public class BinaryMetric {
    private Entity source;
    private Entity target;

    public Entity getTarget() {
        return target;
    }

    public void setTarget(Entity target) {
        this.target = target;
    }

    public Entity getSource() {
        return source;
    }

    public void setSource(Entity source) {
        this.source = source;
    }
}
