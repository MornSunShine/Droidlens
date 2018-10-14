package doridlens.analyse.analyzer;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 20:18
 * Description: 分析器异常,分析APK失败时抛出
 */
public class AnalyzerException extends Exception {

    /**
     * 构造函数
     *
     * @param apk   异常反生的
     * @param cause 引发分析失败的底层异常
     */
    public AnalyzerException(String apk, Throwable cause) {
        super("Unable to analyze " + apk, cause);
    }

    public AnalyzerException(String apk) {
        super("Unable to analyze " + apk);
    }

}