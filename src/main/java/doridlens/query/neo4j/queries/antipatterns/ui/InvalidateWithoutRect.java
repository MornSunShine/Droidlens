package doridlens.query.neo4j.queries.antipatterns.ui;

import doridlens.query.neo4j.queries.PaprikaQuery;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 22:30
 * Description:
 */
public class InvalidateWithoutRect extends PaprikaQuery {

    public static final String KEY = "IWR";

    public InvalidateWithoutRect() {
        super(KEY);
    }

    /*
        MATCH (a:App)-[:APP_OWNS_CLASS]->(:Class{parent_name:'android.view.View'})-[:CLASS_OWNS_METHOD]->
              (n:Method{name:'onDraw'})-[:CALLS]->(e:ExternalMethod{name:'invalidate'})
        WHERE NOT (e)-[:METHOD_OWNS_ARGUMENT]->(:ExternalArgument)
            AND (a.target_sdk < 14)
        RETURN n.app_key

        details -> n.full_name as full_name
        else -> count(n) as IWR
     */

    @Override
    public String getQuery(boolean details) {
        String query = "MATCH (a:App)-[:APP_OWNS_CLASS]->(:Class{parent_name:'android.view.View'})-[:CLASS_OWNS_METHOD]->\n" +
                "(n:Method{name:'onDraw'})-[:CALLS]->(e:ExternalMethod{name:'invalidate'})\n" +
                "WHERE NOT (e)-[:METHOD_OWNS_ARGUMENT]->(:ExternalArgument)\n" +
//                "   AND (a.target_sdk < 14)\n" +
                "RETURN n.app_key as app_key,";
        if (details) {
            query += "n.full_name as full_name";
        } else {
            query += "count(n) as IWR";
        }
        return query;
    }
}