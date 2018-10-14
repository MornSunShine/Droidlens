package doridlens.query.neo4j.queries.antipatterns;

import doridlens.analyse.metrics.methods.stat.NumberOfParameters;
import doridlens.query.neo4j.queries.PaprikaQuery;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 22:22
 * Description: LongParameterList查询
 */
public class LongParameterList extends PaprikaQuery {

    public static final String KEY = "LPL";

    private static final int THRESHOLD = 5;

    public LongParameterList() {
        super(KEY);
    }

    @Override
    public String getQuery(boolean details) {
        String query = "MATCH (m:Method) \n" +
                "WHERE m." + NumberOfParameters.NAME + " > " + THRESHOLD + "\n" +
                "RETURN m.app_key AS app_key,";
        if (details) {
            query += "m.full_name AS full_name," +
                    " m." + NumberOfParameters.NAME + " AS " + NumberOfParameters.NAME;
        } else {
            query += "count(m) AS LPL";
        }
        return query;
    }
}
