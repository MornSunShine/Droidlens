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
 * Time: 22:27
 * Description:
 */
public class SwissArmyKnife extends FuzzyQuery {

    public static final String KEY = "SAK";

    public SwissArmyKnife(QueryPropertiesReader reader) {
        super(KEY, "SwissArmyKnife.fcl", reader);
    }

    /*
        MATCH (cl:Class)
        WHERE exists(cl.is_interface)
            AND cl.number_of_methods > veryHigh
        RETURN cl.app_key as app_key

        details -> cl.name as full_name
        else -> count(cl) as SAK
     */

    @Override
    public String getQuery(boolean details) {
        String query = getSAKNodes(reader.get("SAK_methods_veryHigh"));
        query += "RETURN cl.app_key as app_key,";
        if (details) {
            query +="cl.name as full_name";
        } else {
            query += " count(cl) as SAK";
        }
        return query;
    }

    /*
        MATCH (cl:Class)
        WHERE exists(cl.is_interface)
            AND cl.number_of_methods > high
        RETURN cl.app_key as app_key,cl.number_of_methods as number_of_methods

        details -> cl.name as full_name
     */

    @Override
    public String getFuzzyQuery(boolean details) {
        String query = getSAKNodes(reader.get("SAK_methods_high"));
        query += "RETURN cl.app_key as app_key,cl.number_of_methods as number_of_methods";
        if (details) {
            query +=",cl.name as full_name";
        }
        return query;
    }

    private String getSAKNodes(double threshold) {
        return "MATCH (cl:Class)\n" +
                "WHERE exists(cl.is_interface)\n" +
                "   AND cl.number_of_methods > " + threshold + "\n";
    }

    @Override
    public List<Map<String, Object>> getFuzzyResult(List<Map<String, Object>> result, FIS fis) {
        int cc;
        List<Map<String, Object>> fuzzyResult = new ArrayList<>();
        FunctionBlock fb = fis.getFunctionBlock(null);
        for (Map<String, Object> res : result) {
            cc = (int) res.get("number_of_methods");
            if (cc >= reader.get("SAK_methods_veryHigh")) {
                res.put("fuzzy_value", 1);
            } else {
                fb.setVariable("number_of_methods", cc);
                fb.evaluate();
                res.put("fuzzy_value", fb.getVariable("res").getValue());
            }
            fuzzyResult.add(res);
        }
        return fuzzyResult;
    }
}
