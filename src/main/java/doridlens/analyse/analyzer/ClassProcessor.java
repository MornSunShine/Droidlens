package doridlens.analyse.analyzer;

import doridlens.analyse.entities.PaprikaClass;
import doridlens.analyse.entities.PaprikaVariable;
import doridlens.analyse.metrics.app.NumberOfClasses;
import doridlens.analyse.metrics.app.NumberOfVariables;
import doridlens.analyse.metrics.classes.condition.counted.*;
import doridlens.analyse.metrics.classes.condition.counted.subclass.*;
import doridlens.analyse.metrics.classes.stat.soot.*;
import doridlens.analyse.metrics.common.CommonCondition;
import doridlens.analyse.metrics.common.IsFinal;
import doridlens.analyse.metrics.common.IsStatic;
import doridlens.analyse.metrics.common.NumberOfMethods;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.util.Chain;

import java.util.Map;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 20:19
 * Description: 类加工器,在应用调用图谱构建完成之前,使用Soot对类进行加工
 */
public class ClassProcessor {

    private CountedClassCondition[] classConditions = {
            new IsAbstractClass(),
            new IsInterface(),
            new IsInnerClassStatic()
    };

    private IsSubClass[] subClassConditions = {
            new IsActivity(),
            new IsApplication(),
            new IsAsyncTask(),
            new IsBroadcastReceiver(),
            new IsContentProvider(),
            new IsService(),
            new IsView()
    };

    private CommonCondition[] conditions = {
            new IsFinal(), // This must stay at index 0
            new IsStatic()
    };

    private SootClassStatistic[] statistics = {
            new DepthOfInheritance(),
            new NumberOfAttributes(),
            new ImplementedInterfaces(),
            new NumberOfMethods()
    };

    private PaprikaContainer container;
    private Map<SootClass, PaprikaClass> classMap;
    private boolean mainPackageOnly;
    private int varCount = 0;

    /**
     * 构造函数
     *
     * @param container       用于保存应用信息的容器实体
     * @param mainPackageOnly 是否分析主包外的类
     */
    public ClassProcessor(PaprikaContainer container, boolean mainPackageOnly) {
        this.container = container;
        this.classMap = container.getClassMap();
        this.mainPackageOnly = mainPackageOnly;
    }

    /**
     * 加工应用中所有的类,完成后收集所有类及其子类的统计信息
     */
    public void processClasses() {
        Chain<SootClass> sootClasses = Scene.v().getApplicationClasses();
        String pack = container.getPaprikaApp().getPackage();
        String rsubClassStart = pack + ".R$";
        String packs = pack.concat(".");
        String buildConfigClass = pack.concat(".BuildConfig");
        for (SootClass sootClass : sootClasses) {
            String name = sootClass.getName();
            if (name.startsWith(rsubClassStart) || name.equals(buildConfigClass)) {
                continue;
            }
            if (!mainPackageOnly || name.startsWith(packs)) {
                collectClassMetrics(sootClass);
            }
        }
        // Now that all classes have been processed at least once (and the map filled) we can process NOC
        for (SootClass sootClass : sootClasses) {
            if (sootClass.hasSuperclass()) {
                SootClass superClass = sootClass.getSuperclass();
                PaprikaClass paprikaClass = classMap.get(superClass);
                if (paprikaClass != null) classMap.get(superClass).addChildren();
            }
        }
        collectAppMetrics();
    }

    /**
     * 注册类信息到Paprika应用模型,并收集相关度量数据
     *
     * @param sootClass 类的Soot表现
     */
    private void collectClassMetrics(SootClass sootClass) {
        PaprikaClass paprikaClass = container.addClass(sootClass);
        // Checking if the class is final
        conditions[0].createIfMatching(sootClass, paprikaClass);
        // Checking if the class is a child of a relevant subclass
        for (IsSubClass subClass : subClassConditions) {
            if (subClass.createIfMatching(sootClass, paprikaClass)) {
                break;
            }
        }
        for (CountedClassCondition condition : classConditions) {
            condition.createIfMatching(sootClass, paprikaClass);
        }
        // Field analysis
        sootClass.getFields().forEach(field -> registerField(paprikaClass, field));
        // Numerical stats
        for (SootClassStatistic stat : statistics) {
            stat.collectMetric(sootClass, paprikaClass);
        }
    }

    /**
     * 添加类域信息到应用模型,并收集相关度量数据
     *
     * @param paprikaClass 域归属的类对象实例
     * @param sootField    Soot域对象
     */
    private void registerField(PaprikaClass paprikaClass, SootField sootField) {
        PaprikaVariable paprikaVariable = container.addField(paprikaClass, sootField);
        varCount++;
        for (CommonCondition condition : conditions) {
            condition.createIfMatching(sootField, paprikaVariable);
        }
    }

    /**
     * 收集类的统计信息
     * 仅当所有类都被加工之后,才调用
     */
    private void collectAppMetrics() {
        NumberOfClasses.createMetric(container.getPaprikaApp(), classMap.size());
        NumberOfVariables.createMetric(container.getPaprikaApp(), varCount);
        for (CountedClassCondition condition : classConditions) {
            condition.createNumberMetric(container.getPaprikaApp());
        }
        for (IsSubClass condition : subClassConditions) {
            condition.createNumberMetric(container.getPaprikaApp());
        }
    }

}
