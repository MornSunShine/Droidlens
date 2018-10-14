package doridlens.query.neo4j.queries.antipatterns.memory;

import doridlens.query.neo4j.queries.PaprikaQuery;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 22:28
 * Description:
 */
public class LeakingInnerClass extends PaprikaQuery {

    public static final String KEY = "LIC";

    public LeakingInnerClass() {
        super(KEY);
    }

    /*
        MATCH (cl:Class) WHERE exists(cl.is_inner_class)
        AND NOT exists(cl.is_static)
        RETURN cl.app_key as app_key

        details -> cl.name as full_name
        else -> count(cl) as LIC
     */

    @Override
    public String getQuery(boolean details) {
        String query = "MATCH (cl:Class) WHERE exists(cl.is_inner_class)\n" +
                "   AND NOT exists(cl.is_static)\n" +
                "RETURN cl.app_key as app_key,";
        if (details) {
            query += "cl.name as full_name";
        } else {
            query += "count(cl) as LIC";
        }
        return query;
    }

}
