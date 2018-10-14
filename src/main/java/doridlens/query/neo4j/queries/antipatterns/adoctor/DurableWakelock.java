package doridlens.query.neo4j.queries.antipatterns.adoctor;

import doridlens.query.neo4j.queries.PaprikaQuery;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 22:23
 * Description:
 */
public class DurableWakelock extends PaprikaQuery {

    public static final String KEY = "DW";

    public DurableWakelock() {
        super(KEY);
    }

    @Override
    public String getQuery(boolean details) {
        String query = "MATCH (m:Method)-[:CALLS]->(e:ExternalMethod {full_name:'acquire#android.os.PowerManager$WakeLock'}) " +
                "WHERE NOT (e)-[:METHOD_OWNS_ARGUMENT]->(:ExternalArgument) " +
                "AND NOT (m)-[:CALLS]->(:ExternalMethod {full_name:'release#android.os.PowerManager$WakeLock'}) " +
                "RETURN m.app_key AS app_key,";
        if (details) {
            query += "m.full_name AS full_name";
        } else {
            query += "count(m) AS DW";
        }
        return query;
    }

}
