package doridlens.analyse.analyzer;

import doridlens.analyse.entities.*;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 20:25
 * Description: Paprika容器
 *              储存Soot和Paprika对象进行应用分析所需要的数据结构
 */
public class PaprikaContainer {

    private Map<SootClass, PaprikaClass> classMap;
    private Map<SootMethod, PaprikaMethod> methodMap;
    private Map<SootClass, PaprikaExternalClass> externalClassMap;
    private Map<SootMethod, PaprikaExternalMethod> externalMethodMap;

    private PaprikaApp paprikaApp;

    /**
     * 构造函数
     *
     * @param app 待分析的APP实体实例
     */
    public PaprikaContainer(PaprikaApp app) {
        this.paprikaApp = app;
        this.classMap = new HashMap<>();
        this.methodMap = new HashMap<>();
        this.externalClassMap = new HashMap<>();
        this.externalMethodMap = new HashMap<>();
    }

    public PaprikaApp getPaprikaApp() {
        return paprikaApp;
    }

    public Map<SootClass, PaprikaClass> getClassMap() {
        return classMap;
    }

    public Map<SootMethod, PaprikaMethod> getMethodMap() {
        return methodMap;
    }

    public Map<SootMethod, PaprikaExternalMethod> getExternalMethodMap() {
        return externalMethodMap;
    }

    /**
     * 向应用模型中添加新的类
     *
     * @param sootClass 类的Soot呈现
     * @return 类的Paprika表现
     */
    public PaprikaClass addClass(SootClass sootClass) {
        PaprikaClass paprikaClass = PaprikaClass.create(sootClass.getName(),
                paprikaApp, PaprikaModifier.getModifier(sootClass));
        if (sootClass.hasSuperclass()) {
            paprikaClass.setParentName(sootClass.getSuperclass().getName());
        }
        this.classMap.put(sootClass, paprikaClass);
        return paprikaClass;
    }

    /**
     * 添加新的域信息到应用模型
     *
     * @param paprikaClass 待添加域的Paprika类对象实例
     * @param sootField    域的Soot表现
     * @return 域的Paprika变量实例
     */
    public PaprikaVariable addField(PaprikaClass paprikaClass, SootField sootField) {
        return PaprikaVariable.create(
                sootField.getName(), sootField.getType().toString(), PaprikaModifier.getModifier(sootField),
                paprikaClass);
    }

    /**
     * 添加新的方法到应用模型
     * 在执行{@link MethodProcessor}进行加工时,所有的方法都应执行过该方法
     *
     * @param sootMethod 函数的Soot表现
     */
    public void addMethod(SootMethod sootMethod) {
        SootClass sootClass = sootMethod.getDeclaringClass();
        PaprikaClass paprikaClass = classMap.get(sootClass);
        if (paprikaClass == null) {
            // Should be R or external classes
            try {
                sootClass.setLibraryClass();
            } catch (NullPointerException e) {
                // Soot issue. Can be safely ignored.
            }
            return;
        }
        PaprikaMethod paprikaMethod = PaprikaMethod.create(sootMethod.getName(),
                PaprikaModifier.getModifier(sootMethod),
                sootMethod.getReturnType().toString(), paprikaClass);
        methodMap.put(sootMethod, paprikaMethod);
    }

    /**
     * 从应用模型提取外部类,如果没找到,添加到模型
     * @param sootClass 外部类的Soot表现
     * @return 外部类的Paprika表现
     */
    public PaprikaExternalClass getOrCreateExternalClass(SootClass sootClass) {
        PaprikaExternalClass paprikaExternalClass = externalClassMap.get(sootClass);
        if (paprikaExternalClass == null) {
            paprikaExternalClass = PaprikaExternalClass.create(sootClass.getName(), paprikaApp);
            externalClassMap.put(sootClass, paprikaExternalClass);
        }
        return paprikaExternalClass;
    }

    /**
     * 构建应用的类层次
     */
    public void computeInheritance() {
        for (Map.Entry entry : classMap.entrySet()) {
            SootClass sClass = (SootClass) entry.getKey();
            PaprikaClass pClass = (PaprikaClass) entry.getValue();
            SootClass sParent = sClass.getSuperclass();
            PaprikaClass pParent = classMap.get(sParent);
            if (pParent != null) {
                pClass.setParent(pParent);
            }
        }
    }

    /**
     * 链接应用的接口到对应实现的类
     */
    public void computeInterface() {
        for (Map.Entry entry : classMap.entrySet()) {
            SootClass sClass = (SootClass) entry.getKey();
            PaprikaClass pClass = (PaprikaClass) entry.getValue();
            for (SootClass SInterface : sClass.getInterfaces()) {
                PaprikaClass pInterface = classMap.get(SInterface);
                if (pInterface != null) {
                    pClass.implement(pInterface);
                }
            }
        }
    }
}
