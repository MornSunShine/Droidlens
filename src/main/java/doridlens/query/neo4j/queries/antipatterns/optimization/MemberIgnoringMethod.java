package doridlens.query.neo4j.queries.antipatterns.optimization;

import doridlens.query.neo4j.queries.PaprikaQuery;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 22:30
 * Description:
 */
public class MemberIgnoringMethod extends PaprikaQuery {

    public static final String KEY = "MIM";

    public MemberIgnoringMethod() {
        super(KEY);
    }

    /*
        MATCH (m1:Method)
        WHERE m1.number_of_callers > 0 AND NOT exists(m1.is_static)
            AND NOT exists(m1.is_override)
            AND NOT (m1)-[:USES]->(:Variable)
            AND NOT (m1)-[:CALLS]->(:ExternalMethod)
            AND NOT (m1)-[:CALLS]->(:Method)
            AND NOT exists(m1.is_init)
        RETURN m1.app_key as app_key

        details -> m1.full_name as full_name
        else -> count(m1) as MIM
     */

    @Override
    public String getQuery(boolean details) {
        String query = " MATCH (m1:Method)\n" +
                "WHERE m1.number_of_callers > 0 AND NOT exists(m1.is_static)\n" +
                "   AND NOT exists(m1.is_override)\n" +
                "   AND NOT (m1)-[:USES]->(:Variable)\n" +
                "   AND NOT (m1)-[:CALLS]->(:ExternalMethod)\n" +
                "   AND NOT (m1)-[:CALLS]->(:Method)\n" +
                "   AND NOT exists(m1.is_init)\n" +
                "RETURN m1.app_key as app_key,";
        if (details) {
            query += " m1.full_name as full_name";
        } else {
            query += "count(m1) as MIM";
        }
        return query;
    }

}
