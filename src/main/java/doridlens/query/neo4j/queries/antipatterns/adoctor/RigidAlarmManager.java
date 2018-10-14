package doridlens.query.neo4j.queries.antipatterns.adoctor;

import doridlens.query.neo4j.queries.PaprikaQuery;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 22:24
 * Description:
 */
public class RigidAlarmManager extends PaprikaQuery {

    public static final String KEY = "RAM";

    public RigidAlarmManager() {
        super(KEY);
    }

    @Override
    public String getQuery(boolean details) {
        String query = "MATCH (a:App)-[:APP_OWNS_CLASS]->(:Class)-[:CLASS_OWNS_METHOD]->" +
                "(m:Method)-[:CALLS]->(:ExternalMethod {full_name:'setRepeating#android.app.AlarmManager'}) " +
                "WHERE a.target_sdk < 19 " +
                "RETURN a.app_key AS app_key,";
        if (details) {
            query += "m.full_name AS full_name";
        } else {
            query += "count(m) AS RAM";
        }
        return query;
    }
}
