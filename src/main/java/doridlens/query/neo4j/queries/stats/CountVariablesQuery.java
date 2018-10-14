package doridlens.query.neo4j.queries.stats;

import doridlens.query.neo4j.queries.PaprikaQuery;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 22:17
 * Description:
 */
public class CountVariablesQuery extends PaprikaQuery {

    public static final String COMMAND_KEY = "COUNTVAR";

    public CountVariablesQuery() {
        super("COUNT_VARIABLE");
    }

    /*
        MATCH (n:Variable)
        RETURN n.app_key as app_key, count(n) as nb_variables
     */

    @Override
    public String getQuery(boolean details) {
        return "MATCH (n:Variable)\n" +
                "RETURN n.app_key as app_key, count(n) as nb_variables";
    }

}
