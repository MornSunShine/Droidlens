package doridlens.query.neo4j.queries.antipatterns.optimization;

import doridlens.query.neo4j.queries.PaprikaQuery;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 22:30
 * Description:
 */
public class InternalGetterSetter extends PaprikaQuery {

    public static final String KEY = "IGS";

    public InternalGetterSetter() {
        super(KEY);
    }

    /*
     MATCH (a:App) WITH a.app_key as key
     MATCH (cl:Class {app_key: key})-[:CLASS_OWNS_METHOD]->(m1:Method {app_key: key})-[:CALLS]->(m2:Method {app_key: key})
     WHERE (m2.is_setter OR m2.is_getter) AND (cl)-[:CLASS_OWNS_METHOD]->(m2)
     RETURN m1.app_key as app_key

     details -> m1.full_name as full_name
                m2.full_name as gs_name
     else -> count(m1) as IGS
     */

    @Override
    public String getQuery(boolean details) {
        String query = "MATCH (a:App) WITH a.app_key as key\n" +
                "MATCH (cl:Class {app_key: key})-[:CLASS_OWNS_METHOD]->(m1:Method {app_key: key})-[:CALLS]->(m2:Method {app_key: key})\n" +
                "WHERE (m2.is_setter OR m2.is_getter) AND (cl)-[:CLASS_OWNS_METHOD]->(m2)\n" +
                "RETURN m1.app_key as app_key,";
        if (details) {
            query += "m1.full_name as full_name, m2.full_name as gs_name";
        } else {
            query += "count(m1) as IGS";
        }
        return query;
    }

}