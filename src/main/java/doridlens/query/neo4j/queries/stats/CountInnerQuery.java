package doridlens.query.neo4j.queries.stats;

import doridlens.query.neo4j.queries.PaprikaQuery;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 22:17
 * Description:
 */
public class CountInnerQuery extends PaprikaQuery {

    public static final String COMMAND_KEY = "COUNTINNER";

    public CountInnerQuery() {
        super("COUNT_INNER");
    }

    /*
        MATCH (n:Class) WHERE exists(n.is_inner_class)
        RETURN n.app_key as app_key,count(n) as nb_inner_classes
     */

    @Override
    public String getQuery(boolean details) {
        return " MATCH (n:Class) WHERE exists(n.is_inner_class)\n" +
                "RETURN n.app_key as app_key,count(n) as nb_inner_classes";
    }

}
