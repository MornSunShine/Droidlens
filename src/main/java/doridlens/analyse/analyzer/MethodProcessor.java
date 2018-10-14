package doridlens.analyse.analyzer;

import doridlens.analyse.entities.*;
import doridlens.analyse.metrics.common.*;
import doridlens.analyse.metrics.methods.NumberOfCallers;
import doridlens.analyse.metrics.methods.NumberOfDirectCalls;
import doridlens.analyse.metrics.methods.condition.IsSynchronized;
import doridlens.analyse.metrics.methods.stat.NumberOfParameters;
import soot.Scene;
import soot.SootMethod;
import soot.Type;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;

import java.util.Iterator;
import java.util.Map;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 20:25
 * Description: 函数处理,在应用的调用图谱构建完成之后,对其中的函数进行深入处理加工
 */
public class MethodProcessor {

    private PaprikaContainer container;
    private Map<SootMethod, PaprikaMethod> methodMap;
    private Map<SootMethod, PaprikaExternalMethod> externalMethodMap;
    private MethodBodyProcessor bodyProcessor;

    private CommonCondition[] commonConditions = {
            new IsStatic(),
            new IsFinal(),
            new IsAbstract()
    };

    private IsSynchronized isSynchronized;
    private NumberOfParameters parameters;


    /**
     * 构造函数
     *
     * @param container 包含被分析应用信息的容器实例
     */
    public MethodProcessor(PaprikaContainer container) {
        this.container = container;
        this.methodMap = container.getMethodMap();
        this.externalMethodMap = container.getExternalMethodMap();
        this.isSynchronized = new IsSynchronized();
        this.parameters = new NumberOfParameters();
        this.bodyProcessor = new MethodBodyProcessor();
    }

    /**
     * 加工应用的中的所有函数
     * 在这之前,应用的中的所有函数,应必须都通过{@link PaprikaContainer#addMethod(SootMethod)}在容器中注册
     */
    public void processMethods() {
        CallGraph callGraph = Scene.v().getCallGraph();
        for (Map.Entry<SootMethod, PaprikaMethod> entry : methodMap.entrySet()) {
            collectStandardMetrics(entry.getKey(), entry.getValue());
            collectCallGraphMetrics(callGraph, entry.getKey(), entry.getValue());
        }
        NumberOfMethods.createNumberOfMethods(container.getPaprikaApp(), methodMap.size());
    }

    /**
     * 收集与关系图无关的函数度量数据
     *
     * @param sootMethod    the Soot representation of the method
     * @param paprikaMethod the Paprika representation of the method
     */
    private void collectStandardMetrics(SootMethod sootMethod, PaprikaMethod paprikaMethod) {
        for (CommonCondition condition : commonConditions) {
            condition.createIfMatching(sootMethod, paprikaMethod);
        }
        isSynchronized.createIfMatching(sootMethod, paprikaMethod);
        parameters.collectMetric(sootMethod, paprikaMethod);
        if (sootMethod.hasActiveBody()) {
            bodyProcessor.processMethodBody(sootMethod, paprikaMethod);
        }
    }

    /**
     * 收集与Soot关系图相关的函数度量数据
     *
     * @param callGraph     Soot关系图模型
     * @param sootMethod    the Soot representation of the method
     * @param paprikaMethod the Paprika representation of the method
     */
    private void collectCallGraphMetrics(CallGraph callGraph, SootMethod sootMethod, PaprikaMethod paprikaMethod) {
        int edgeOutCount = 0;
        int edgeIntoCount = 0;
        Iterator<Edge> edgeOutIterator = callGraph.edgesOutOf(sootMethod);
        Iterator<Edge> edgeIntoIterator = callGraph.edgesInto(sootMethod);
        PaprikaClass currentClass = paprikaMethod.getPaprikaClass();

        while (edgeOutIterator.hasNext()) {
            Edge edgeOut = edgeOutIterator.next();
            SootMethod target = edgeOut.tgt();
            PaprikaMethod targetMethod = methodMap.get(target);
            // In the case we are calling an external method (sdk or library)
            if (targetMethod == null) {
                analyzeExternalCall(target, paprikaMethod);
            } else {
                paprikaMethod.callMethod(targetMethod);
            }
            PaprikaClass targetClass = container.getClassMap().get(target.getDeclaringClass());
            if (edgeOut.isVirtual() || edgeOut.isSpecial() || edgeOut.isStatic()) {
                edgeOutCount++;
            }
            // Detecting coupling (may include calls to inherited methods)
            if (targetClass != null && targetClass != currentClass) {
                currentClass.coupledTo(targetClass);
            }
        }

        while (edgeIntoIterator.hasNext()) {
            Edge e = edgeIntoIterator.next();
            if (e.isExplicit()) edgeIntoCount++;
        }
        NumberOfDirectCalls.createMetric(paprikaMethod, edgeOutCount);
        NumberOfCallers.createMetric(paprikaMethod, edgeIntoCount);
    }

    /**
     * Add an external call to a library to the Paprika application model.
     *
     * @param target        the Soot representation of the library method that was called
     * @param paprikaMethod the Paprika method calling the library method
     */
    private void analyzeExternalCall(SootMethod target, PaprikaMethod paprikaMethod) {
        PaprikaExternalMethod externalTgtMethod = externalMethodMap.get(target);
        if (externalTgtMethod == null) {
            PaprikaExternalClass paprikaExternalClass = container.getOrCreateExternalClass(target.getDeclaringClass());
            externalTgtMethod = PaprikaExternalMethod.create(target.getName(),
                    target.getReturnType().toString(), paprikaExternalClass);
            int i = 0;
            for (Type type : target.getParameterTypes()) {
                i++;
                PaprikaExternalArgument.create(type.toString(), i, externalTgtMethod);
            }
            externalMethodMap.put(target, externalTgtMethod);
        }
        paprikaMethod.callMethod(externalTgtMethod);
    }
}
