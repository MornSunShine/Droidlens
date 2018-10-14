package doridlens.query.neo4j.queries.antipatterns.ui;

import doridlens.query.neo4j.queries.PaprikaQuery;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 22:31
 * Description:
 */
public class Overdraw extends PaprikaQuery {

    public static final String KEY = "UIO";

    public Overdraw() {
        super(KEY);
    }

    /*
        MATCH (:Class{parent_name:"android.view.View"})-[:CLASS_OWNS_METHOD]->(n:Method{name:"onDraw"})
            -[:METHOD_OWNS_ARGUMENT]->(:Argument{position:1,name:"android.graphics.Canvas"})
        WHERE NOT (n)-[:CALLS]->(:ExternalMethod{full_name:"clipRect#android.graphics.Canvas"})
            AND NOT (n)-[:CALLS]->(:ExternalMethod{full_name:"quickReject#android.graphics.Canvas"})
            AND NOT (n)-[:CALLS]->(:ExternalMethod{full_name:"clipOutRect#android.graphics.Canvas"})
        RETURN n.app_key as app_key

        details -> n.full_name as full_name
        else -> count(n) as UIO
     */

    @Override
    public String getQuery(boolean details) {
        String query = "MATCH (:Class{parent_name:\"android.view.View\"})-[:CLASS_OWNS_METHOD]->(n:Method{name:\"onDraw\"})\n" +
                "            -[:METHOD_OWNS_ARGUMENT]->(:Argument{position:1,name:\"android.graphics.Canvas\"})\n" +
                "WHERE NOT (n)-[:CALLS]->(:ExternalMethod{full_name:\"clipRect#android.graphics.Canvas\"})\n" +
                "   AND NOT (n)-[:CALLS]->(:ExternalMethod{full_name:\"quickReject#android.graphics.Canvas\"})\n" +
                "   AND NOT (n)-[:CALLS]->(:ExternalMethod{full_name:\"clipOutRect#android.graphics.Canvas\"})\n" +
                "RETURN n.app_key as app_key,";
        if (details) {
            query += "n.full_name as full_name";
        } else {
            query += "count(n) as UIO";
        }

        return query;
    }

}
