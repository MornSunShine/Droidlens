package doridlens.query.neo4j.queries.antipatterns.memory;

import doridlens.query.neo4j.queries.PaprikaQuery;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 22:29
 * Description:
 */
public class UnsuitedLRUCache extends PaprikaQuery {

    public static final String KEY = "UCS";

    public UnsuitedLRUCache() {
        super(KEY);
    }

    /*
        MATCH (m:Method)-[:CALLS]->(e:ExternalMethod {full_name:'<init>#android.util.LruCache'})
        WHERE NOT (m)-[:CALLS]->(:ExternalMethod {full_name:'getMemoryClass#android.app.ActivityManager'})
            AND NOT (m)-[:CALLS]->(:ExternalMethod {full_name:'getMemoryInfo#android.app.ActivityManager'})
            AND NOT (m)-[:CALLS]->(:ExternalMethod {full_name:'maxMemory#java.lang.Runtime'})
        RETURN m.app_key as app_key

        details -> m.full_name as full_name
        else -> count(m) as UCS
     */

    @Override
    public String getQuery(boolean details) {
        String query = "MATCH (m:Method)-[:CALLS]->(e:ExternalMethod {full_name:'<init>#android.util.LruCache'})\n" +
                "WHERE NOT (m)-[:CALLS]->(:ExternalMethod {full_name:'getMemoryClass#android.app.ActivityManager'})\n" +
                "   AND NOT (m)-[:CALLS]->(:ExternalMethod {full_name:'getMemoryInfo#android.app.ActivityManager'})\n" +
                "   AND NOT (m)-[:CALLS]->(:ExternalMethod {full_name:'maxMemory#java.lang.Runtime'})\n" +
                "RETURN m.app_key as app_key,";
        if(details) {
            query += " m.full_name as full_name";
        } else {
            query += "count(m) as UCS";
        }
        return query;
    }

}