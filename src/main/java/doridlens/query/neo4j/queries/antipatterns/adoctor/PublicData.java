package doridlens.query.neo4j.queries.antipatterns.adoctor;

import doridlens.analyse.metrics.methods.condition.UsesPublicData;
import doridlens.query.neo4j.queries.PaprikaQuery;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 22:24
 * Description:
 */
public class PublicData extends PaprikaQuery {

    public static final String KEY = "PD";

    public PublicData() {
        super(KEY);
    }

    @Override
    public String getQuery(boolean details) {
        String query = "MATCH (m:Method) " +
                "WHERE m." + UsesPublicData.NAME + "\n" +
                "RETURN m.app_key AS app_key,";
        if (details) {
            query += "m.full_name AS full_name";
        } else {
            query += "count(m) AS PD";
        }
        return query;
    }

}
