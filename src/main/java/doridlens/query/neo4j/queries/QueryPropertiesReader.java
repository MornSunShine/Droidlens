package doridlens.query.neo4j.queries;

import com.sun.istack.internal.Nullable;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 21:26
 * Description: Query属性读取器,从.properties文件载入属性信息用作模糊查询
 * 所有包括在属性文件中的信息将被自动载入,所有数据都应该能被转型为Double
 */
public class QueryPropertiesReader {

    public static final String DEFAULT_PROPS = "thresholds.properties";

    private Map<String, Double> properties = new HashMap<>();

    /**
     * 从文件中载入模糊属性
     *
     * @param propsArg 属性文件路径,若空,使用默认属性数据
     * @throws IOException              读取或载入文件失败,则抛出异常
     * @throws QueryPropertiesException 不能进行Double转型,则抛出异常
     */
    public void loadProperties(@Nullable String propsArg) throws IOException, QueryPropertiesException {
        Properties props = new Properties();
        if (propsArg != null) {
            props.load(new FileInputStream(propsArg));
        } else {
            props.load(QueryPropertiesReader.class
                    .getClassLoader().getResourceAsStream(DEFAULT_PROPS));
        }
        for (Map.Entry<Object, Object> entry : props.entrySet()) {
            try {
                properties.put(entry.getKey().toString(), Double.valueOf(entry.getValue().toString()));
            } catch (NumberFormatException e) {
                throw new QueryPropertiesException(propsArg, entry.getKey().toString(), e);
            }
        }
    }

    /**
     * 替换字符串中所有的属性名称为其对应的属性值
     * {@link #loadProperties(String)}必须在这之前被调用
     * 样例,字符串(Class_complexity_high, 0)将被替换为(28, 0)
     *
     * @param function 待替换属性名称的文本
     * @return 属性名称被替换成值后的字符串
     */
    public String replaceProperties(String function) {
        for (Map.Entry<String, Double> entry : properties.entrySet()) {
            if (function.contains(entry.getKey())) {
                function = function.replaceAll(entry.getKey(), Double.toString(entry.getValue()));
            }
        }
        return function;
    }

    /**
     * 根据属性名称取出属性值
     *
     * @param name 属性名称
     * @return 属性对应的值, 未找到则为NULL
     * @throws NullPointerException 属性未被提前载入，则抛出异常
     */
    public double get(String name) {
        return properties.get(name);
    }

}
