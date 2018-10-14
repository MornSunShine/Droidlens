package doridlens.analyse.analyzer;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 20:19
 * Description: 异常类,负责AndroidManifest.xml文件解析失败的异常处理
 */
public class ManifestException extends AnalyzerException {

    /**
     * 构造函数
     *
     * @param apk   分析的APK
     * @param cause 引发异常的底层异常实例
     */
    public ManifestException(String apk, Throwable cause) {
        super("The manifest from " + apk + " could not be parsed.", cause);
    }

}