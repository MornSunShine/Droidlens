package doridlens.analyse;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static doridlens.launcher.arg.Argument.*;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 19:56
 * Description: 从格式化的JSON文件读取APP属性信息
 * JSON格式参考README
 */
public class ApkPropertiesParser {

    public static final String PROPS_FILENAME = "apk-properties.json";

    private static final List<String> PROPS_KEYS = Arrays.asList(
            NAME_ARG.toString(), PACKAGE_ARG.toString(), KEY_ARG.toString(), DEVELOPER_ARG.toString(), CATEGORY_ARG.toString(),
            NB_DOWNLOAD_ARG.toString(), DATE_ARG.toString(), RATING_ARG.toString(), SIZE_ARG.toString(), VERSION_CODE_ARG.toString(),
            VERSION_NAME_ARG.toString(), TARGET_SDK_VERSION_ARG.toString(), SDK_VERSION_ARG.toString(),
            ONLY_MAIN_PACKAGE_ARG.toString(), PRICE_ARG.toString()
    );

    private Map<String, Map<String, String>> appProperties;
    private String jsonPath;
    private PrintStream out;

    /**
     * 构造函数
     *
     * @param out      反馈输出流
     * @param jsonPath JSON文件路径
     * @param apps     APK文件名称列表
     * @throws PropertiesException APK文件名与属性名冲突,则抛出异常
     */
    public ApkPropertiesParser(PrintStream out, String jsonPath, List<String> apps) throws PropertiesException {
        this.jsonPath = jsonPath;
        this.out = out;
        appProperties = new HashMap<>();
        for (String app : apps) {
            if (PROPS_KEYS.contains(app)) {
                throw new PropertiesException("'" + app + ".apk' is not a valid apk name for the properties json, " +
                        "please rename the file");
            }
            appProperties.put(app, new HashMap<>());
        }
    }

    /**
     * 从JSON文件载入属性信息
     *
     * @throws IOException         读取文件失败,则抛出异常
     * @throws PropertiesException 属性不存在,则抛出异常
     */
    public void readProperties() throws IOException, PropertiesException {
        File jsonFile = new File(jsonPath);
        if (!jsonFile.exists()) {
            out.println("No " + PROPS_FILENAME + " found.");
            return;
        } else {
            out.println("Parsing " + PROPS_FILENAME + "...");
        }
        JsonObject root = Json.parse(new FileReader(jsonFile)).asObject();
        try {
            processAlternativeSyntax(root);
            processBasicSyntax(root);
        } catch (NullPointerException e) {
            throw new PropertiesException(e);
        }
    }

    /**
     * 备选语法检索操作过程
     *
     * @param root JSON的根节点
     * @throws PropertiesException JSON属性文件操作异常,则抛出
     */
    private void processAlternativeSyntax(JsonObject root) throws PropertiesException {
        for (String key : PROPS_KEYS) {
            JsonValue propsValues = root.get(key);
            if (propsValues == null) {
                continue;
            }
            for (JsonValue propsValuesItem : propsValues.asArray()) {
                // { value:"myOtherCategory", apps:[ "anApk" ] },
                JsonObject valueObject = propsValuesItem.asObject();
                String value = valueObject.get("value").asString();
                for (JsonValue appValue : valueObject.get("apps").asArray()) {
                    insertProperty(appValue.asString(), key, value);
                }
            }
        }
    }


    /**
     * 基础语法检索过程
     *
     * @param root JSON文件根节点
     * @throws PropertiesException 对应APP的属性不存在,则抛出异常
     */
    private void processBasicSyntax(JsonObject root) throws PropertiesException {
        for (String appName : appProperties.keySet()) {
            JsonValue appObjectValue = root.get(appName);
            if (appObjectValue == null) {
                continue;
            }
            JsonObject appObject = appObjectValue.asObject();
            for (String key : PROPS_KEYS) {
                JsonValue propertyValue = appObject.get(key);
                if (propertyValue != null) {
                    insertProperty(appName, key, propertyValue.asString());
                }
            }
        }
    }

    /**
     * 插入属性
     *
     * @param app      APP名称
     * @param property 属性名,KEY
     * @param value    属性值,VALUE
     * @throws PropertiesException 对应APP的属性不存在,则抛出异常
     */
    private void insertProperty(String app, String property, String value) throws PropertiesException {
        Map<String, String> apkProps = appProperties.get(app);
        if (apkProps == null) {
            throw new PropertiesException("The application " + app +
                    " referred to in the JSON was not found in the folder");
        }
        apkProps.put(property, value);
    }


    /**
     * 获取APP通过JSON属性文件载入的属性
     * 必须在@link #readProperties()}载入属性之后才能调用本方法
     *
     * @param app      APK名称
     * @param property 待取属性名称,比如rating等
     * @return 属性值, 如果没有设置或未匹配对应的APP, 则为NULL
     */
    public String getAppProperty(String app, String property) {
        Map<String, String> requestedProps = appProperties.get(app);
        if (requestedProps == null) {
            return null;
        }
        return requestedProps.get(property);
    }

    /**
     * 检查JSON属性文件中,APP的属性是否已被设置
     * 必须在{@link #readProperties()}之后调用载入属性之后,再调用本函数
     *
     * @param app APK名称
     * @return True, 属性已被设置, 否则为
     */
    public boolean hasProperties(String app) {
        Map<String, String> props = appProperties.get(app);
        if (props == null) {
            return false;
        }
        return !props.isEmpty();
    }

}

