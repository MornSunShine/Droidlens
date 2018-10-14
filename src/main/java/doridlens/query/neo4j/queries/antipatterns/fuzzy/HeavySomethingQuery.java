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
public abstract class HeavySomethingQuery extends FuzzyQuery {

    public HeavySomethingQuery(String queryName, QueryPropertiesReader reader) {
        super(queryName, "HeavySomething.fcl", reader);
    }

    @Override
    public List<Map<String, Object>> getFuzzyResult(List<Map<String, Object>> result, FIS fis) {
        int noi, cc;
        List<Map<String, Object>> fuzzyResult = new ArrayList<>();
        FunctionBlock fb = fis.getFunctionBlock(null);
        for (Map<String, Object> res : result) {
            cc = (int) res.get("cyclomatic_complexity");
            noi = (int) res.get("number_of_instructions");
            if (cc >= reader.get("Heavy_class_veryHigh_cc")
                    && noi >= reader.get("Heavy_class_veryHigh_noi")) {
                res.put("fuzzy_value", 1);
            } else {
                fb.setVariable("cyclomatic_complexity", cc);
                fb.setVariable("number_of_instructions", noi);
                fb.evaluate();
                res.put("fuzzy_value", fb.getVariable("res").getValue());
            }
            fuzzyResult.add(res);
        }
        return fuzzyResult;
    }

}
