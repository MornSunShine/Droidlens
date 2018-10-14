package doridlens.analyse.neo4j;

import doridlens.analyse.entities.*;
import doridlens.analyse.metrics.common.IsStatic;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.schema.Schema;

import static doridlens.analyse.neo4j.ModelToGraph.*;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 20:43
 * Description:
 */
public class IndexManager {

    private GraphDatabaseService graphDatabaseService;

    public IndexManager(GraphDatabaseService graphDatabaseService) {
        this.graphDatabaseService = graphDatabaseService;
    }

    public void createIndex() {
        try (Transaction tx = graphDatabaseService.beginTx()) {
            Schema schema = graphDatabaseService.schema();
            if (schema.getIndexes(VARIABLE_LABEL).iterator().hasNext()) {
                schema.indexFor(VARIABLE_LABEL)
                        .on(PaprikaVariable.N4J_APP_KEY)
                        .create();
            }
            if (schema.getIndexes(METHOD_LABEL).iterator().hasNext()) {
                schema.indexFor(METHOD_LABEL)
                        .on(PaprikaMethod.N4J_APP_KEY)
                        .create();
                schema.indexFor(METHOD_LABEL)
                        .on(IsStatic.NAME)
                        .create();
            }
            if (schema.getIndexes(ARGUMENT_LABEL).iterator().hasNext()) {
                schema.indexFor(ARGUMENT_LABEL)
                        .on(PaprikaArgument.N4J_APP_KEY)
                        .create();
            }
            if (schema.getIndexes(EXTERNAL_CLASS_LABEL).iterator().hasNext()) {
                schema.indexFor(EXTERNAL_CLASS_LABEL)
                        .on(PaprikaExternalClass.N4J_APP_KEY)
                        .create();
            }
            if (schema.getIndexes(EXTERNAL_METHOD_LABEL).iterator().hasNext()) {
                schema.indexFor(EXTERNAL_METHOD_LABEL)
                        .on(PaprikaExternalMethod.N4J_APP_KEY)
                        .create();
            }
            tx.success();
        }
        try (Transaction tx = graphDatabaseService.beginTx()) {
            org.neo4j.graphdb.index.IndexManager index = graphDatabaseService.index();
            if (!index.existsForRelationships(RelationTypes.CALLS.name())) {
                index.forRelationships(RelationTypes.CALLS.name());
            }
            tx.success();
        }
    }
}
