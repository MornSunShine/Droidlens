package doridlens.query.neo4j.queries.stats;

import doridlens.query.neo4j.queries.PaprikaQuery;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 22:18
 * Description:
 */
public class CountViewsQuery extends PaprikaQuery {

    public static final String COMMAND_KEY = "COUNTVIEWS";

    public CountViewsQuery() {
        super("COUNT_VIEWS");
    }

    /*
        MATCH (n:Class{parent_name:'android.view.View'})
        RETURN n.app_key as app_key,count(n) as number_of_views
     */

    @Override
    public String getQuery(boolean details) {
        return " MATCH (n:Class{parent_name:'android.view.View'})\n" +
                "RETURN n.app_key as app_key,count(n) as number_of_views";
    }
}
