package doridlens.analyse.analyzer;

import doridlens.analyse.entities.PaprikaApp;
import doridlens.analyse.metrics.app.IsDebuggableRelease;
import doridlens.analyse.metrics.app.IsSetConfigChanges;
import net.dongliu.apk.parser.ApkFile;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 20:20
 * Description: 处理应用的AndroidManifest.xml文件
 */
public class ManifestProcessor {

    private static final String APP_NODE = "/manifest/application";
    private static final String DEBUG_ATTRIBUTE = "android:debuggable";
    private static final String CONFIG_ATTRIBUTE = "android:configChanges";

    private String apkPath;
    private PaprikaApp app;

    /**
     * 构造函数
     *
     * @param app     对应的应用实体实例
     * @param apkPath APK文件路径
     */
    public ManifestProcessor(PaprikaApp app, String apkPath) {
        this.apkPath = apkPath;
        this.app = app;
    }

    /**
     * 提取分析应用的AndroidManifest.xml文件
     *
     * @throws ManifestException 解析失败,抛出异常
     */
    public void parseManifest() throws ManifestException {
        try (ApkFile apkFile = new ApkFile(apkPath)) {
            parseManifestText(apkFile.getManifestXml());
        } catch (IOException e) {
            throw new ManifestException(apkPath, e);
        }
    }

    /**
     * 解析字符串化的AndroidManifest.xml文件
     *
     * @param text AndroidManifest.xml的文本化字符串
     * @throws ManifestException 解析失败,抛出异常
     */
    public void parseManifestText(String text) throws ManifestException {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document root = builder.parse(new ByteArrayInputStream(text.getBytes()));
            XPath xPath = XPathFactory.newInstance().newXPath();
            Node appNode = (Node) xPath.compile(APP_NODE).evaluate(root, XPathConstants.NODE);
            Node attribute = appNode.getAttributes().getNamedItem(DEBUG_ATTRIBUTE);
            if (attribute != null && Boolean.valueOf(attribute.getNodeValue())) {
                IsDebuggableRelease.createMetric(app);
            }
            attribute = appNode.getAttributes().getNamedItem(CONFIG_ATTRIBUTE);
            if (attribute != null && Boolean.valueOf(attribute.getNodeValue())) {
                IsSetConfigChanges.createMetric(app);
            }
        } catch (IOException | ParserConfigurationException
                | SAXException | XPathExpressionException e) {
            throw new ManifestException(apkPath, e);
        }
    }
}
