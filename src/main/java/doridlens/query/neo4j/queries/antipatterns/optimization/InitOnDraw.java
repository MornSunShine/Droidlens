package doridlens.query.neo4j.queries.antipatterns.optimization;

import doridlens.query.neo4j.queries.PaprikaQuery;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 22:29
 * Description:
 */
public class InitOnDraw extends PaprikaQuery {

    public static final String KEY = "IOD";

    public InitOnDraw() {
        super(KEY);
    }

    /*
     MATCH (:Class{parent_name:'android.view.View'})-[:CLASS_OWNS_METHOD]->
        (n:Method{name:'onDraw'})-[:CALLS]->({name:'<init>'})
     RETURN n.app_key as app_key

     details -> n.full_name as full_name
     else -> count(n) as IOD
     */

    @Override
    public String getQuery(boolean details) {
        String query = "MATCH (:Class{parent_name:'android.view.View'})-[:CLASS_OWNS_METHOD]->\n" +
                "   (n:Method{name:'onDraw'})-[:CALLS]->({name:'<init>'})\n" +
                "RETURN n.app_key as app_key,";
        if (details) {
            query += "n.full_name as full_name";
        } else {
            query += "count(n) as IOD";
        }
        return query;
    }

}
