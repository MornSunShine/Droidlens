package doridlens.analyse.metrics;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 20:40
 * Description:
 */
public abstract class Metric<E> {

    protected E value;
    protected String name;

    public Metric(String name, E value) {
        this.name = name;
        this.value = value;
    }

    public E getValue() {
        return value;
    }

    public void setValue(E value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }
}
