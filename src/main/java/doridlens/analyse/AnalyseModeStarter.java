package doridlens.analyse;

import com.sun.istack.internal.Nullable;
import doridlens.analyse.analyzer.AnalyzerException;
import doridlens.analyse.analyzer.SootAnalyzer;
import doridlens.analyse.entities.PaprikaApp;
import doridlens.analyse.neo4j.ModelToGraph;
import doridlens.launcher.PaprikaStarter;
import doridlens.launcher.arg.PaprikaArgParser;
import org.neo4j.graphdb.TransactionFailureException;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static doridlens.launcher.arg.Argument.*;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 19:55
 * Description: Analyse模式启动器,启动对APK文件的分析过程
 */
public class AnalyseModeStarter extends PaprikaStarter {

    private static final int SOOT_RETRIES = 3;
    private static final int NEO4J_RETRIES = 3;

    private PrintStream originalOut;
    private PrintStream originalErr;

    /**
     * 构造函数,默认System.out和System.err为标准的输出
     *
     * @param argParser Paprika参数解析器
     * @param out       用作反馈的输出流
     */
    public AnalyseModeStarter(PaprikaArgParser argParser, PrintStream out) {
        super(argParser, out);
        // Hack to prevent soot from spamming System.out and System.err
        originalOut = System.out;
        System.setOut(new PrintStream(new OutputStream() {
            public void write(int b) {
                // NO-OP
            }
        }));
        originalErr = System.err;
        System.setErr(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                // NO-OP
            }
        }));
    }

    /**
     * 开始对一个或多个APK进行分析
     */
    @Override
    public void start() {
        ModelToGraph modelToGraph = new ModelToGraph(argParser.getArg(DATABASE_ARG));
        List<String> appsPaths = argParser.getAppsPaths();
        if (argParser.isFolderMode()) {
            out.println("Analyzing all apps in folder " + argParser.getArg(APK_ARG));
            ApkPropertiesParser propsParser = null;
            try {
                propsParser = new ApkPropertiesParser(out, argParser.getArg(APK_ARG) + "/"
                        + ApkPropertiesParser.PROPS_FILENAME, argParser.getAppsFileNames(appsPaths));
                propsParser.readProperties();
            } catch (PropertiesException | IOException e) {
                e.printStackTrace(out);
            }
            for (String apk : appsPaths) {
                processApp(apk, modelToGraph, propsParser);
                out.println("Done");
                restoreDefaultStreams();
            }
        } else {
            processApp(argParser.getArg(APK_ARG), modelToGraph, null);
            out.println("Done");
            restoreDefaultStreams();
        }
    }

    /**
     * 默认反馈输出流复位
     */
    private void restoreDefaultStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    /**
     * 分析APK,并存放数据至Neo4j数据库
     *
     * @param apk          APK文件路径
     * @param modelToGraph Neo4j操作实例
     * @param propsParser  JSON属性解析器
     */
    private void processApp(String apk, ModelToGraph modelToGraph, @Nullable ApkPropertiesParser propsParser) {
        try {
            PaprikaApp app = analyzeApp(apk, propsParser);
            saveIntoDatabase(app, modelToGraph, 0);
        } catch (AnalyzerException e) {
            notifyAnalysisFailure(apk, e);
        }
    }

    /**
     * 分析APK文件,并将其转化为对应的Paprika应用模型
     *
     * @param apkPath     APK文件路径
     * @param propsParser 用于插入APP属性的JSON解析器实例
     * @return 分析APK获得的PaprikaApp模型实例
     * @throws AnalyzerException Soot分析APK失败,则抛出异常
     */
    public PaprikaApp analyzeApp(String apkPath, @Nullable ApkPropertiesParser propsParser)
            throws AnalyzerException {
        return analyze(apkPath, propsParser, 0);
    }

    /**
     * APK静态分析的主要过程
     * 1.基础数据填充,根据CLI参数、AndroidManifest.xml文件、用户编写的JSON属性文件完成对PaprikaApp实例的初步填充
     * 2.APK静态分析,通过Soot对APK进行详细的分析,分析对象包括Class,Method和AndroidManifest.xml文件
     *
     * @param apkPath     APK文件路径
     * @param propsParser JSON属性文件解析器
     * @param retries     Soot分析次数
     * @return Paprika应用实体
     * @throws AnalyzerException 分析失败,抛出异常
     */
    private PaprikaApp analyze(String apkPath, @Nullable ApkPropertiesParser propsParser, int retries)
            throws AnalyzerException {
        try {
            out.println("Analyzing " + new File(apkPath).getName());
            out.println("Collecting metrics");
            SootAnalyzer analyzer = new SootAnalyzer(apkPath, argParser.getArg(ANDROID_JARS_ARG));
            PaprikaAppCreator creator = new PaprikaAppCreator(argParser, apkPath);
            try {
                analyzer.prepareSoot();//Soot分析参数初始化
            } catch (RuntimeException e) {
//                e.printStackTrace(out);
                out.println("Soot could not parse the path of the apk.");
                throw new AnalyzerException(apkPath, e);
            }
            creator.readAppInfo();//通过CLI参数载入应用基本信息
            try {
                creator.fetchMissingAppInfo();//通过APK再次载入数据
            } catch (NumberFormatException e) {
                out.println("The Android minimum or target sdk could not be parsed");
                throw new AnalyzerException(apkPath, e);
            }
            if (propsParser != null) {
                creator.addApkProperties(propsParser);//通过JSON属性文件,最后载入数据
            }
            PaprikaApp app = creator.createApp();//创建包含基本信息的Paprika APP实体,此时还没有对代码进行静态分析
            if (!argParser.getFlagArg(FORCE_ANALYSIS_ARG)
                    && (app.getTargetSdkVersion() >= 26 || app.getSdkVersion() >= 26)) {
                out.println("As of 08/2018, Soot has issues analyzing apps using an sdk >= 26");
                out.println("The app " + apkPath + " will not be analyzed.");
                throw new AnalyzerException(apkPath);
            }
            try {
                analyzer.runAnalysis(app, argParser.getFlagArg(ONLY_MAIN_PACKAGE_ARG));//对APK文件进行静态分析
            } catch (RuntimeException e) {
                if (retries < SOOT_RETRIES) {
                    // Soot, please stop crashing randomly. We'll try this again.
                    out.println("Encountered soot issue on app " + apkPath);
                    out.println("Restarting soot analysis...");
                    return analyze(apkPath, propsParser, retries + 1);
                } else {
                    out.println("Soot could not analyze " + apkPath);
                    throw new AnalyzerException(apkPath, e);
                }
            }
            return analyzer.getPaprikaApp();
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new AnalyzerException(apkPath, e);
        }
    }

    /**
     * 保存数据到Neo4J数据库
     * 分析过程的最终步骤,将PaprikaApp对象中数据存放到Neo4J数据库中
     *
     * @param app          应用实体实例
     * @param modelToGraph Neo4J数据库实例
     * @param retries      插入失败后重试次数
     */
    private void saveIntoDatabase(PaprikaApp app, ModelToGraph modelToGraph, int retries) {
        try {
            out.println("Saving into database " + argParser.getArg(DATABASE_ARG));
            modelToGraph.insertApp(app);
        } catch (TransactionFailureException e) {
            if (retries < NEO4J_RETRIES) {
                out.println("Failed to insert into database");
                out.println("Trying again...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException interrupt) {
                    out.println("Interrupted while waiting to try a new transaction");
                    Thread.currentThread().interrupt();
                }
                saveIntoDatabase(app, modelToGraph, retries + 1);
            }
        }
    }

    /**
     * 分析失败消息通知
     *
     * @param apk APK文件路径
     * @param e   异常实例
     */
    private void notifyAnalysisFailure(String apk, Exception e) {
        out.println("Failed to analyze " + apk);
        if (e.getCause() != null) {
            out.println(e.getCause().getMessage());
        }
    }
}

