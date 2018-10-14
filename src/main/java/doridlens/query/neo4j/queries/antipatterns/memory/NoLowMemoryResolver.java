package doridlens.query.neo4j.queries.antipatterns.memory;

import doridlens.query.neo4j.queries.PaprikaQuery;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 22:29
 * Description:
 */
public class NoLowMemoryResolver extends PaprikaQuery {

    public static final String KEY = "NLMR";

    public NoLowMemoryResolver() {
        super(KEY);
    }

    /*
        MATCH (cl:Class)
        WHERE ( exists(cl.is_activity) OR exists(cl.is_application) OR exists (cl.is_service)
            OR exists(cl.is_content_provider))
            AND NOT (cl:Class)-[:CLASS_OWNS_METHOD]->(:Method {name = 'onLowMemory'})
            AND NOT (cl:Class)-[:CLASS_OWNS_METHOD]->(:Method {name = 'onTrimMemory'})
            AND NOT (cl)-[:EXTENDS]->(:Class)
        RETURN cl.app_key as app_key

        details -> cl.name as full_name
        else -> count(cl) as NLMR
     */

    @Override
    public String getQuery(boolean details) {
        String query = "MATCH (cl:Class)\n" +
                "WHERE (cl.is_activity OR cl.is_application OR cl.is_service \n" +
                "       OR cl.is_content_provider)\n" +
                "   AND NOT (cl:Class)-[:CLASS_OWNS_METHOD]->(:Method {name: 'onLowMemory'})\n" +
                "   AND NOT (cl:Class)-[:CLASS_OWNS_METHOD]->(:Method {name: 'onTrimMemory'})\n" +
                "   AND NOT (cl)-[:EXTENDS]->(:Class)\n" +
                "RETURN cl.app_key as app_key,";
        if (details) {
            query += "cl.name as full_name";
        } else {
            query += "count(cl) as NLMR";
        }
        return query;
    }

}
