package doridlens.analyse.analyzer;

import doridlens.analyse.entities.PaprikaApp;
import doridlens.analyse.metrics.classes.stat.paprika.*;
import soot.*;
import soot.options.Options;

import java.io.File;
import java.util.*;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 20:26
 * Description: 分析Android APK,添加应用的模型信息到PaprikaAPP实例中
 */
public class SootAnalyzer {

    private String apk;
    private String androidJAR;
    private PaprikaContainer container;

    private List<PaprikaClassStatistic> finalMetrics = Arrays.asList(
            new ClassComplexity(),
            new CouplingBetweenObjects(),
            new LackOfCohesionInMethods(),
            new NPathComplexity(), // Must be done after class complexity has been processed
            new NumberOfChildren()
    );

    /**
     * 构造函数
     *
     * @param apk        APK路径
     * @param androidJAR Android平台版本目录路径
     */
    public SootAnalyzer(String apk, String androidJAR) {
        this.apk = apk;
        this.androidJAR = androidJAR;
    }

    /**
     * 载入Soot参数,Soot的准备工作
     */
    public void prepareSoot() {
        G.reset();
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_whole_program(true);
        Options.v().set_android_jars(androidJAR);//
        Options.v().set_src_prec(Options.src_prec_apk);//设置分析对象为APK文件
        Options.v().set_process_dir(Collections.singletonList(apk));
        Options.v().set_output_format(Options.output_format_grimple);//设置输出形式
        Options.v().set_output_dir(System.getProperty("user.home")+ File.separator + "/These/decompiler/out");//设置输出目录
        Options.v().set_process_multiple_dex(true);
        Options.v().set_throw_analysis(Options.throw_analysis_dalvik);
        Options.v().set_no_bodies_for_excluded(true);
        PhaseOptions.v().setPhaseOption("cg", "all-reachable:true");
        PhaseOptions.v().setPhaseOption("gop", "enabled:true");
        List<String> excludeList = new LinkedList<>();
        excludeList.add("java.*");
        excludeList.add("sun.misc.*");
        excludeList.add("android.*");
        excludeList.add("org.apache.*");
        excludeList.add("soot.*");
        excludeList.add("javax.servlet.*");
        Options.v().set_exclude(excludeList);
        Scene.v().loadNecessaryClasses();
    }

    /**
     * 分析Android APK,并将应用模型与及其度量数据同Paprika APP进行连接
     *
     * @param app             与被分析APK对应的PaprikaApp实体实例
     * @param mainPackageOnly 是否只分析主包内的类(排除第三方依赖中的类信息)
     * @throws AnalyzerException APK分析失败,则抛出
     */
    public void runAnalysis(PaprikaApp app, boolean mainPackageOnly) throws AnalyzerException {
        this.container = new PaprikaContainer(app);
        ManifestProcessor manifestProcessor = new ManifestProcessor(container.getPaprikaApp(), apk);
        ClassProcessor classProcessor = new ClassProcessor(container, mainPackageOnly);
        MethodProcessor methodProcessor = new MethodProcessor(container);
        manifestProcessor.parseManifest();
        classProcessor.processClasses();
        PackManager.v().getPack("gop").add(new Transform("gop.myInstrumenter", new BodyTransformer() {
            @Override
            protected void internalTransform(final Body body, String phaseName, @SuppressWarnings("rawtypes") Map options) {
                container.addMethod(body.getMethod());
            }
        }));
        PackManager.v().runPacks();
        methodProcessor.processMethods();
        computeFinalMetrics();
    }

    /**
     * 最后调用,计算层次、接口数量的统计等
     */
    public void computeFinalMetrics() {
        container.computeInheritance();
        container.computeInterface();
        container.getPaprikaApp().getPaprikaClasses()
                .forEach(paprikaClass -> finalMetrics
                        .forEach(metric -> metric.collectMetric(paprikaClass)));
    }

    public PaprikaApp getPaprikaApp() {
        return container.getPaprikaApp();
    }

}
