package doridlens.query.neo4j.queries.stats;

import doridlens.query.neo4j.queries.PaprikaQuery;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 22:18
 * Description:
 */
public class PropertyQuery extends PaprikaQuery {

    public static final String ALL_LCOM = "ALLLCOM";
    public static final String ALL_CYCLOMATIC = "ALLCYCLO";
    public static final String ALL_CC = "ALLCC";
    public static final String ALL_METHODS = "ALLNUMMETHODS";

    private String property;
    private String nodeType;

    public PropertyQuery(String queryName, String nodeType, String property) {
        super(queryName);
        this.property = property;
        this.nodeType = nodeType;
    }

    /*
        MATCH (n:nodeType)
        RETURN n.app_key as app_key, n.name as name, n.property as property
     */

    @Override
    public String getQuery(boolean details) {
        return "MATCH (n:" + nodeType + ")\n" +
                "RETURN n.app_key as app_key, n.name as name, n." + property + " as " + property;
    }

}