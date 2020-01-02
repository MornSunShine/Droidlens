package doridlens.neo4j;

import doridlens.entity.*;
import doridlens.metrics.Metric;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: MaoMorn
 * Date: 2020/1/2
 * Time: 9:45
 * Description:
 */
public class ModelToGraph {
    private GraphDatabaseService graphDatabaseService;
    private DatabaseManager databaseManager;

    private Map<Entity, Node> methodNodeMap;
    private Map<PaprikaClass, Node> classNodeMap;
    private Map<PaprikaVariable, Node> variableNodeMap;

    public ModelToGraph(String DatabasePath) {
        this.databaseManager = new DatabaseManager(DatabasePath);
        databaseManager.start();
        this.graphDatabaseService = databaseManager.getGraphDatabaseService();
        methodNodeMap = new HashMap<>();
        classNodeMap = new HashMap<>();
        variableNodeMap = new HashMap<>();
        IndexManager indexManager = new IndexManager(graphDatabaseService);
        indexManager.createIndex();
    }

    public Node insertApp(PaprikaApp paprikaApp) {
        Node appNode;
        try (Transaction tx = graphDatabaseService.beginTx()) {
            appNode = graphDatabaseService.createNode(Labels.App);
            appNode.setProperty("name", paprikaApp.getName());
            appNode.setProperty("sdk", paprikaApp.getSdkVersion());
            appNode.setProperty("target_sdk", paprikaApp.getTargetSdkVersion());
            Date date = new Date();
            SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss.S");
            appNode.setProperty("date_analysis", simpleFormat.format(date));
            for (PaprikaClass paprikaClass : paprikaApp.getPaprikaClasses()) {
                appNode.createRelationshipTo(insertClass(paprikaClass), RelationTypes.APP_OWNS_CLASS);
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
        return appNode;
    }

    private void insertMetric(Metric metric, Node node) {
        node.setProperty(metric.getName(), metric.getValue());
    }


    public Node insertClass(PaprikaClass paprikaClass) {
        Node classNode = graphDatabaseService.createNode(Labels.Class);
        classNodeMap.put(paprikaClass, classNode);
        classNode.setProperty("name", paprikaClass.getName());
        classNode.setProperty("modifier", paprikaClass.getModifier().toString().toLowerCase());
        if (paprikaClass.getParentName() != null) {
            classNode.setProperty("parent_name", paprikaClass.getParentName());
        }
        for (PaprikaVariable paprikaVariable : paprikaClass.getPaprikaVariables()) {
            classNode.createRelationshipTo(insertVariable(paprikaVariable), RelationTypes.CLASS_OWNS_VARIABLE);

        }
        for (PaprikaMethod paprikaMethod : paprikaClass.getPaprikaMethods()) {
            classNode.createRelationshipTo(insertMethod(paprikaMethod), RelationTypes.CLASS_OWNS_METHOD);
        }
        for (Metric metric : paprikaClass.getMetrics()) {
            insertMetric(metric, classNode);
        }
        return classNode;
    }

    public Node insertExternalClass(PaprikaExternalClass paprikaClass) {
        Node classNode = graphDatabaseService.createNode(Labels.ExternalClass);
        classNode.setProperty("name", paprikaClass.getName());
        if (paprikaClass.getParentName() != null) {
            classNode.setProperty("parent_name", paprikaClass.getParentName());
        }
        for (PaprikaExternalMethod paprikaExternalMethod : paprikaClass.getPaprikaExternalMethods()) {
            classNode.createRelationshipTo(insertExternalMethod(paprikaExternalMethod), RelationTypes.CLASS_OWNS_METHOD);
        }
        for (Metric metric : paprikaClass.getMetrics()) {
            insertMetric(metric, classNode);
        }
        return classNode;
    }

    public Node insertVariable(PaprikaVariable paprikaVariable) {
        Node variableNode = graphDatabaseService.createNode(Labels.Variable);
        variableNodeMap.put(paprikaVariable, variableNode);
        variableNode.setProperty("name", paprikaVariable.getName());
        variableNode.setProperty("modifier", paprikaVariable.getModifier().toString().toLowerCase());
        variableNode.setProperty("type", paprikaVariable.getType());
        for (Metric metric : paprikaVariable.getMetrics()) {
            insertMetric(metric, variableNode);
        }
        return variableNode;
    }

    public Node insertMethod(PaprikaMethod paprikaMethod) {
        Node methodNode = graphDatabaseService.createNode(Labels.Method);
        methodNodeMap.put(paprikaMethod, methodNode);
        methodNode.setProperty("name", paprikaMethod.getName());
        methodNode.setProperty("modifier", paprikaMethod.getModifier().toString().toLowerCase());
        methodNode.setProperty("full_name", paprikaMethod.toString());
        methodNode.setProperty("return_type", paprikaMethod.getReturnType());
        for (Metric metric : paprikaMethod.getMetrics()) {
            insertMetric(metric, methodNode);
        }
        for (PaprikaVariable paprikaVariable : paprikaMethod.getUsedVariables()) {
            methodNode.createRelationshipTo(variableNodeMap.get(paprikaVariable), RelationTypes.USES);
        }
        for (PaprikaArgument arg : paprikaMethod.getArguments()) {
            methodNode.createRelationshipTo(insertArgument(arg), RelationTypes.METHOD_OWNS_ARGUMENT);
        }
        return methodNode;
    }

    public Node insertExternalMethod(PaprikaExternalMethod paprikaMethod) {
        Node methodNode = graphDatabaseService.createNode(Labels.ExternalMethod);
        methodNodeMap.put(paprikaMethod, methodNode);
        methodNode.setProperty("name", paprikaMethod.getName());
        methodNode.setProperty("full_name", paprikaMethod.toString());
        methodNode.setProperty("return_type", paprikaMethod.getReturnType());
        for (Metric metric : paprikaMethod.getMetrics()) {
            insertMetric(metric, methodNode);
        }
        for (PaprikaExternalArgument arg : paprikaMethod.getPaprikaExternalArguments()) {
            methodNode.createRelationshipTo(insertExternalArgument(arg), RelationTypes.METHOD_OWNS_ARGUMENT);
        }
        return methodNode;
    }

    public Node insertArgument(PaprikaArgument paprikaArgument) {
        Node argNode = graphDatabaseService.createNode(Labels.Argument);
        argNode.setProperty("name", paprikaArgument.getName());
        argNode.setProperty("position", paprikaArgument.getPosition());
        return argNode;
    }

    public Node insertExternalArgument(PaprikaExternalArgument paprikaExternalArgument) {
        Node argNode = graphDatabaseService.createNode(Labels.ExternalArgument);
        argNode.setProperty("name", paprikaExternalArgument.getName());
        argNode.setProperty("position", paprikaExternalArgument.getPosition());
        for (Metric metric : paprikaExternalArgument.getMetrics()) {
            insertMetric(metric, argNode);
        }
        return argNode;
    }

    public void createHierarchy(PaprikaApp paprikaApp) {
        for (PaprikaClass paprikaClass : paprikaApp.getPaprikaClasses()) {
            PaprikaClass parent = paprikaClass.getParent();
            if (parent != null) {
                classNodeMap.get(paprikaClass).createRelationshipTo(classNodeMap.get(parent), RelationTypes.EXTENDS);
            }
            for (PaprikaClass pInterface : paprikaClass.getInterfaces()) {
                classNodeMap.get(paprikaClass).createRelationshipTo(classNodeMap.get(pInterface), RelationTypes.IMPLEMENTS);
            }
        }
    }

    public void createCallGraph(PaprikaApp paprikaApp) {
        for (PaprikaClass paprikaClass : paprikaApp.getPaprikaClasses()) {
            for (PaprikaMethod paprikaMethod : paprikaClass.getPaprikaMethods()) {
                for (Entity calledMethod : paprikaMethod.getCalledMethods()) {
                    methodNodeMap.get(paprikaMethod).createRelationshipTo(methodNodeMap.get(calledMethod), RelationTypes.CALLS);
                }
            }
        }
    }
}

