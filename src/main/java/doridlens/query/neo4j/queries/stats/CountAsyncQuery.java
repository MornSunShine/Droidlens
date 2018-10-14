package doridlens.query.neo4j.queries.stats;

import doridlens.query.neo4j.queries.PaprikaQuery;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 22:15
 * Description:
 */
public class CountAsyncQuery extends PaprikaQuery {

    public static final String COMMAND_KEY = "COUNTASYNC";

    public CountAsyncQuery() {
        super("COUNT_ASYNC");
    }

    /*
        MATCH (n:Class{parent_name:'android.os.AsyncTask'})
        RETURN n.app_key as app_key,count(n) as number_of_async
     */

    @Override
    public String getQuery(boolean details) {
        return "MATCH (n:Class{parent_name:'android.os.AsyncTask'})\n" +
                "RETURN n.app_key as app_key,count(n) as number_of_async";
    }

}
