package doridlens.analyse.neo4j;

import doridlens.DatabaseManager;
import doridlens.analyse.entities.*;
import doridlens.analyse.metrics.Metric;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static doridlens.analyse.neo4j.RelationTypes.*;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 20:43
 * Description:
 */
public class ModelToGraph {

    public static final String APP_TYPE = "App";
    public static final String CLASS_TYPE = "Class";
    public static final String EXTERNAL_CLASS_TYPE = "ExternalClass";
    public static final String METHOD_TYPE = "Method";
    public static final String EXTERNAL_METHOD_TYPE = "ExternalMethod";
    public static final String VARIABLE_TYPE = "Variable";
    public static final String ARGUMENT_TYPE = "Argument";
    public static final String EXTERNAL_ARGUMENT_TYPE = "ExternalArgument";

    public static final Label APP_LABEL = Label.label(APP_TYPE);
    public static final Label CLASS_LABEL = Label.label(CLASS_TYPE);
    public static final Label EXTERNAL_CLASS_LABEL = Label.label(EXTERNAL_CLASS_TYPE);
    public static final Label METHOD_LABEL = Label.label(METHOD_TYPE);
    public static final Label EXTERNAL_METHOD_LABEL = Label.label(EXTERNAL_METHOD_TYPE);
    public static final Label VARIABLE_LABEL = Label.label(VARIABLE_TYPE);
    public static final Label ARGUMENT_LABEL = Label.label(ARGUMENT_TYPE);
    public static final Label EXTERNAL_ARGUMENT_LABEL = Label.label(EXTERNAL_ARGUMENT_TYPE);

    private GraphDatabaseService graphDatabaseService;
    private Map<Entity, Node> methodNodeMap;
    private Map<PaprikaClass, Node> classNodeMap;
    private Map<PaprikaVariable, Node> variableNodeMap;
    private String key;

    public ModelToGraph(String databasePath) {
        DatabaseManager databaseManager = new DatabaseManager(databasePath);
        databaseManager.start();
        this.graphDatabaseService = databaseManager.getGraphDatabaseService();
        methodNodeMap = new HashMap<>();
        classNodeMap = new HashMap<>();
        variableNodeMap = new HashMap<>();
        IndexManager indexManager = new IndexManager(graphDatabaseService);
        indexManager.createIndex();
    }

    public void insertApp(PaprikaApp paprikaApp) {
        this.key = paprikaApp.getKey();
        Node appNode;
        try (Transaction tx = graphDatabaseService.beginTx()) {
            appNode = graphDatabaseService.createNode(APP_LABEL);
            appNode.setProperty(PaprikaApp.N4J_APP_KEY, key);
            appNode.setProperty(PaprikaApp.N4J_NAME, paprikaApp.getName());
            appNode.setProperty(PaprikaApp.N4J_CATEGORY, paprikaApp.getCategory());
            appNode.setProperty(PaprikaApp.N4J_PACKAGE, paprikaApp.getPackage());
            appNode.setProperty(PaprikaApp.N4J_DEVELOPER, paprikaApp.getDeveloper());
            appNode.setProperty(PaprikaApp.N4J_RATING, paprikaApp.getRating());
            appNode.setProperty(PaprikaApp.N4J_NB_DOWN, paprikaApp.getNbDownload());
            appNode.setProperty(PaprikaApp.N4J_DATE_DOWN, paprikaApp.getDate());
            appNode.setProperty(PaprikaApp.N4J_VERSION_CODE, paprikaApp.getVersionCode());
            appNode.setProperty(PaprikaApp.N4J_VERSION_NAME, paprikaApp.getVersionName());
            appNode.setProperty(PaprikaApp.N4J_SDK, paprikaApp.getSdkVersion());
            appNode.setProperty(PaprikaApp.N4J_TARGET_SDK, paprikaApp.getTargetSdkVersion());
            Date date = new Date();
            SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss.S");
            appNode.setProperty(PaprikaApp.N4J_DATE_ANALYSIS, simpleFormat.format(date));
            appNode.setProperty(PaprikaApp.N4J_SIZE, paprikaApp.getSize());
            appNode.setProperty(PaprikaApp.N4J_PRICE, paprikaApp.getPrice());
            for (PaprikaClass paprikaClass : paprikaApp.getPaprikaClasses()) {
                appNode.createRelationshipTo(insertClass(paprikaClass), APP_OWNS_CLASS);
            }
            for (PaprikaExternalClass paprikaExternalClass : paprikaApp.getPaprikaExternalClasses()) {
                insertExternalClass(paprikaExternalClass);
            }
            for (Metric metric : paprikaApp.getMetrics()) {
                insertMetric(metric, appNode);
            }
            tx.success();
        }
        try (Transaction tx = graphDatabaseService.beginTx()) {
            createHierarchy(paprikaApp);
            createCallGraph(paprikaApp);
            tx.success();
        }
    }

    private void insertMetric(Metric metric, Node node) {
        node.setProperty(metric.getName(), metric.getValue());
    }

    public Node insertClass(PaprikaClass paprikaClass) {
        Node classNode = graphDatabaseService.createNode(CLASS_LABEL);
        classNodeMap.put(paprikaClass, classNode);
        classNode.setProperty(PaprikaClass.N4J_APP_KEY, key);
        classNode.setProperty(PaprikaClass.N4J_NAME, paprikaClass.getName());
        classNode.setProperty(PaprikaClass.N4J_MODIFIER, paprikaClass.getModifierAsString());
        if (paprikaClass.getParentName() != null) {
            classNode.setProperty(PaprikaClass.N4J_PARENT, paprikaClass.getParentName());
        }
        for (PaprikaVariable paprikaVariable : paprikaClass.getPaprikaVariables()) {
            classNode.createRelationshipTo(insertVariable(paprikaVariable), CLASS_OWNS_VARIABLE);
        }
        for (PaprikaMethod paprikaMethod : paprikaClass.getPaprikaMethods()) {
            classNode.createRelationshipTo(insertMethod(paprikaMethod), CLASS_OWNS_METHOD);
        }
        for (Metric metric : paprikaClass.getMetrics()) {
            insertMetric(metric, classNode);
        }
        return classNode;
    }

    public void insertExternalClass(PaprikaExternalClass paprikaClass) {
        Node classNode = graphDatabaseService.createNode(EXTERNAL_CLASS_LABEL);
        classNode.setProperty(PaprikaExternalClass.N4J_APP_KEY, key);
        classNode.setProperty(PaprikaExternalClass.N4J_NAME, paprikaClass.getName());
        if (paprikaClass.getParentName() != null) {
            classNode.setProperty(PaprikaExternalClass.N4J_PARENT, paprikaClass.getParentName());
        }
        for (PaprikaExternalMethod paprikaExternalMethod : paprikaClass.getPaprikaExternalMethods()) {
            classNode.createRelationshipTo(insertExternalMethod(paprikaExternalMethod), CLASS_OWNS_METHOD);
        }
        for (Metric metric : paprikaClass.getMetrics()) {
            insertMetric(metric, classNode);
        }
    }

    public Node insertVariable(PaprikaVariable paprikaVariable) {
        Node variableNode = graphDatabaseService.createNode(VARIABLE_LABEL);
        variableNodeMap.put(paprikaVariable, variableNode);
        variableNode.setProperty(PaprikaVariable.N4J_APP_KEY, key);
        variableNode.setProperty(PaprikaVariable.N4J_NAME, paprikaVariable.getName());
        variableNode.setProperty(PaprikaVariable.N4J_MODIFIER,
                paprikaVariable.getModifierAsString());
        variableNode.setProperty(PaprikaVariable.N4J_TYPE, paprikaVariable.getType());
        for (Metric metric : paprikaVariable.getMetrics()) {
            insertMetric(metric, variableNode);
        }
        return variableNode;
    }

    public Node insertMethod(PaprikaMethod paprikaMethod) {
        Node methodNode = graphDatabaseService.createNode(METHOD_LABEL);
        methodNodeMap.put(paprikaMethod, methodNode);
        methodNode.setProperty(PaprikaMethod.N4J_APP_KEY, key);
        methodNode.setProperty(PaprikaMethod.N4J_NAME, paprikaMethod.getName());
        methodNode.setProperty(PaprikaMethod.N4J_MODIFIER, paprikaMethod.getModifierAsString());
        methodNode.setProperty(PaprikaMethod.N4J_FULL_NAME, paprikaMethod.toString());
        methodNode.setProperty(PaprikaMethod.N4J_RETURN_TYPE, paprikaMethod.getReturnType());
        for (Metric metric : paprikaMethod.getMetrics()) {
            insertMetric(metric, methodNode);
        }
        for (PaprikaVariable paprikaVariable : paprikaMethod.getUsedVariables()) {
            methodNode.createRelationshipTo(variableNodeMap.get(paprikaVariable), USES);
        }
        for (PaprikaArgument arg : paprikaMethod.getArguments()) {
            methodNode.createRelationshipTo(insertArgument(arg), METHOD_OWNS_ARGUMENT);
        }
        return methodNode;
    }

    public Node insertExternalMethod(PaprikaExternalMethod paprikaMethod) {
        Node methodNode = graphDatabaseService.createNode(EXTERNAL_METHOD_LABEL);
        methodNodeMap.put(paprikaMethod, methodNode);
        methodNode.setProperty(PaprikaExternalMethod.N4J_APP_KEY, key);
        methodNode.setProperty(PaprikaExternalMethod.N4J_NAME, paprikaMethod.getName());
        methodNode.setProperty(PaprikaExternalMethod.N4J_FULL_NAME, paprikaMethod.toString());
        methodNode.setProperty(PaprikaExternalMethod.N4J_RETURN_TYPE, paprikaMethod.getReturnType());
        for (Metric metric : paprikaMethod.getMetrics()) {
            insertMetric(metric, methodNode);
        }
        for (PaprikaExternalArgument arg : paprikaMethod.getPaprikaExternalArguments()) {
            methodNode.createRelationshipTo(insertExternalArgument(arg), METHOD_OWNS_ARGUMENT);
        }
        return methodNode;
    }

    public Node insertArgument(PaprikaArgument paprikaArgument) {
        Node argNode = graphDatabaseService.createNode(ARGUMENT_LABEL);
        argNode.setProperty(PaprikaArgument.N4J_APP_KEY, key);
        argNode.setProperty(PaprikaArgument.N4J_NAME, paprikaArgument.getName());
        argNode.setProperty(PaprikaArgument.N4J_POSITION, paprikaArgument.getPosition());
        return argNode;
    }

    public Node insertExternalArgument(PaprikaExternalArgument paprikaExternalArgument) {
        Node argNode = graphDatabaseService.createNode(EXTERNAL_ARGUMENT_LABEL);
        argNode.setProperty(PaprikaExternalArgument.N4J_APP_KEY, key);
        argNode.setProperty(PaprikaExternalArgument.N4J_NAME, paprikaExternalArgument.getName());
        argNode.setProperty(PaprikaExternalArgument.N4J_POSITION, paprikaExternalArgument.getPosition());
        for (Metric metric : paprikaExternalArgument.getMetrics()) {
            insertMetric(metric, argNode);
        }
        return argNode;
    }

    public void createHierarchy(PaprikaApp paprikaApp) {
        for (PaprikaClass paprikaClass : paprikaApp.getPaprikaClasses()) {
            PaprikaClass parent = paprikaClass.getParent();
            if (parent != null) {
                classNodeMap.get(paprikaClass).createRelationshipTo(classNodeMap.get(parent), EXTENDS);
            }
            for (PaprikaClass pInterface : paprikaClass.getInterfaces()) {
                classNodeMap.get(paprikaClass).createRelationshipTo(classNodeMap.get(pInterface), IMPLEMENTS);
            }
        }
    }

    public void createCallGraph(PaprikaApp paprikaApp) {
        for (PaprikaClass paprikaClass : paprikaApp.getPaprikaClasses()) {
            for (PaprikaMethod paprikaMethod : paprikaClass.getPaprikaMethods()) {
                for (Entity calledMethod : paprikaMethod.getCalledMethods()) {
                    methodNodeMap.get(paprikaMethod).createRelationshipTo(methodNodeMap.get(calledMethod), CALLS);
                }
            }
        }
    }
}
