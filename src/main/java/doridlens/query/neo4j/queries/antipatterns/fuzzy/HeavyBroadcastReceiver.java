package doridlens.query.neo4j.queries.antipatterns.fuzzy;

import doridlens.query.neo4j.queries.QueryPropertiesReader;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 22:26
 * Description:
 */
public class HeavyBroadcastReceiver extends HeavySomethingQuery {

    public static final String KEY = "HBR";

    public HeavyBroadcastReceiver(QueryPropertiesReader reader) {
        super(KEY, reader);
    }

    /*
        MATCH (c:Class{is_broadcast_receiver:true})-[:CLASS_OWNS_METHOD]->(m:Method{name:'onReceive'})
        WHERE m.number_of_instructions > veryHigh_noi
            AND m.cyclomatic_complexity > veryHigh_cc
        RETURN m.app_key as app_key

        details -> m.full_name as full_name
        else -> count(m) as HBR
     */

    @Override
    public String getQuery(boolean details) {
        String query = getHBRNodes(reader.get("Heavy_class_veryHigh_noi"),
                reader.get("Heavy_class_veryHigh_cc"));
        query += "RETURN m.app_key as app_key,";
        if (details) {
            query += "m.full_name as full_name";
        } else {
            query += "count(m) AS HBR";
        }
        return query;
    }

    /*
        MATCH (c:Class{is_broadcast_receiver:true})-[:CLASS_OWNS_METHOD]->(m:Method{name:'onReceive'})
        WHERE m.number_of_instructions > high_noi
            AND m.cyclomatic_complexity > high_cc
        RETURN m.app_key as app_key,m.cyclomatic_complexity as cyclomatic_complexity,
            m.number_of_instructions as number_of_instructions

        details -> m.full_name as full_name
     */

    @Override
    public String getFuzzyQuery(boolean details) {
        String query = getHBRNodes(reader.get("Heavy_class_high_noi"),
                reader.get("Heavy_class_high_cc"));
        query += "RETURN m.app_key as app_key,m.cyclomatic_complexity as cyclomatic_complexity,\n" +
                "m.number_of_instructions as number_of_instructions";
        if (details) {
            query += ",m.full_name as full_name";
        }
        return query;
    }

    private String getHBRNodes(double noiThreshold, double ccThreshold) {
        return "MATCH (c:Class{is_broadcast_receiver:true})-[:CLASS_OWNS_METHOD]->(m:Method{name:'onReceive'})\n" +
                "WHERE m.number_of_instructions > " + noiThreshold + "\n" +
                "   AND m.cyclomatic_complexity > " + ccThreshold + "\n";
    }

}