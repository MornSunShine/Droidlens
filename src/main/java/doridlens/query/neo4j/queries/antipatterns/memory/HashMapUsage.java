package doridlens.query.neo4j.queries.antipatterns.memory;

import doridlens.query.neo4j.queries.PaprikaQuery;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 22:28
 * Description:
 */
public class HashMapUsage extends PaprikaQuery {

    public static final String KEY = "HMU";

    public HashMapUsage() {
        super(KEY);
    }

    /*
      MATCH (m:Method)-[:CALLS]->(e:ExternalMethod{full_name:'<init>#java.util.HashMap'})
      RETURN m.app_key, m.full_name AS full_name

      details -> count(m) as HMU
     */

    @Override
    public String getQuery(boolean details) {
        String query = "MATCH (m:Method)-[:CALLS]->(e:ExternalMethod{full_name:'<init>#java.util.HashMap'})\n" +
                "RETURN m.app_key AS app_key, m.full_name AS full_name";
        if (details) {
            query += ",count(m) as HMU";
        }
        return query;
    }


}