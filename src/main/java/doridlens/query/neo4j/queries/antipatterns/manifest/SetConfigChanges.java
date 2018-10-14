package doridlens.query.neo4j.queries.antipatterns.manifest;

import doridlens.analyse.metrics.app.IsSetConfigChanges;
import doridlens.query.neo4j.queries.PaprikaQuery;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 22:28
 * Description:
 */
public class SetConfigChanges extends PaprikaQuery {
    public static final String KEY = "SCC";

    public SetConfigChanges() {
        super(KEY);
    }

    @Override
    public String getQuery(boolean details) {
        return "MATCH (n:App) " +
                "WHERE n." + IsSetConfigChanges.NAME + "\n" +
                "RETURN n.app_key AS app_key";
    }
}
