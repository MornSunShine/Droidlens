package doridlens.query.neo4j.queries.antipatterns.manifest;

import doridlens.analyse.metrics.app.IsDebuggableRelease;
import doridlens.query.neo4j.queries.PaprikaQuery;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 22:28
 * Description:
 */
public class DebuggableRelease extends PaprikaQuery {

    public static final String KEY = "DR";

    public DebuggableRelease() {
        super(KEY);
    }

    @Override
    public String getQuery(boolean details) {
        return "MATCH (n:App) " +
                "WHERE n." + IsDebuggableRelease.NAME + "\n" +
                "RETURN n.app_key AS app_key";
    }
}
