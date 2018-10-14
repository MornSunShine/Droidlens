package doridlens.query.neo4j.queries.antipatterns.fuzzy;

import doridlens.query.neo4j.queries.QueryPropertiesReader;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 22:25
 * Description:
 */
public class ComplexClass extends FuzzyQuery {

    public static final String KEY = "CC";

    public ComplexClass(QueryPropertiesReader reader) {
        super(KEY, "ComplexClass.fcl", reader);
    }

    /*
        MATCH (cl:Class)
        WHERE cl.class_complexity > veryHigh
        RETURN cl.app_key as app_key

        details -> cl.name as full_name
        else -> count(cl) as CC
     */

    @Override
    public String getQuery(boolean details) {
        String query = getCCNodes(reader.get("Class_complexity_veryHigh"));
        query += "RETURN cl.app_key as app_key,";
        if (details) {
            query += "cl.name as full_name";
        } else {
            query += "count(cl) as CC";
        }
        return query;
    }

    /*
        MATCH (cl:Class) WHERE cl.class_complexity > high
        RETURN cl.app_key as app_key, cl.class_complexity as class_complexity

        details -> cl.name as full_name
     */

    @Override
    public String getFuzzyQuery(boolean details) {
        String query = getCCNodes(reader.get("Class_complexity_high"));
        query += "RETURN cl.app_key as app_key, cl.class_complexity as class_complexity";
        if (details) {
            query += ",cl.name as full_name";
        }
        return query;
    }

    private String getCCNodes(double threshold) {
        return "MATCH (cl:Class)\n" +
                "WHERE cl.class_complexity > " + threshold + "\n";
    }

    @Override
    public List<Map<String, Object>> getFuzzyResult(List<Map<String, Object>> result, FIS fis) {
        int cc;
        List<Map<String, Object>> fuzzyResult = new ArrayList<>();
        FunctionBlock fb = fis.getFunctionBlock(null);
        for (Map<String, Object> res : result) {
            cc = (int) res.get("class_complexity");
            if (cc >= reader.get("Class_complexity_veryHigh")) {
                res.put("fuzzy_value", 1);
            } else {
                fb.setVariable("class_complexity", cc);
                fb.evaluate();
                res.put("fuzzy_value", fb.getVariable("res").getValue());
            }
            fuzzyResult.add(res);
        }
        return fuzzyResult;
    }
}