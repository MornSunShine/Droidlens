package doridlens.query.neo4j;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 21:23
 * Description:
 */
public class QuartileCalculator {

    private GraphDatabaseService graphDatabaseService;
    private CSVWriter writer;

    public QuartileCalculator(QueryEngine queryEngine) {
        graphDatabaseService = queryEngine.getGraphDatabaseService();
        this.writer = new CSVWriter(queryEngine.getCsvPrefix());
    }

    public void calculateClassComplexityQuartile() throws IOException {
        Map<String, Double> res;
        Result result;
        try (Transaction ignored = graphDatabaseService.beginTx()) {
            String query = "MATCH (n:Class) WHERE NOT exists(n.is_interface) AND NOT exists(n.is_abstract) " +
                    "RETURN percentileCont(n.class_complexity,0.25) as Q1, percentileCont(n.class_complexity,0.5)" +
                    " as MED, percentileCont(n.class_complexity,0.75) as Q3";
            result = graphDatabaseService.execute(query);
            res = calculateThresholds(result);
        }
        writer.statsToCSV(res, "_STAT_CLASS_COMPLEXITY.csv");
    }

    public void calculateCyclomaticComplexityQuartile() throws IOException {
        Map<String, Double> res;
        Result result;
        try (Transaction ignored = graphDatabaseService.beginTx()) {
            String query = "MATCH (n:Method) WHERE NOT exists(n.is_getter) AND NOT exists(n.is_setter) AND n.cyclomatic_complexity > 0" +
                    " RETURN percentileCont(n.cyclomatic_complexity,0.25) as Q1, percentileCont(n.cyclomatic_complexity,0.5) as MED," +
                    " percentileCont(n.cyclomatic_complexity,0.75) as Q3";
            result = graphDatabaseService.execute(query);
            res = calculateThresholds(result);
        }
        writer.statsToCSV(res, "_STAT_CYCLOMATIC_COMPLEXITY.csv");
    }

    public void calculateNumberOfInstructionsQuartile() throws IOException {
        Map<String, Double> res;
        Result result;
        try (Transaction ignored = graphDatabaseService.beginTx()) {
            String query = "MATCH (n:Method) WHERE NOT exists(n.is_getter) AND NOT exists(n.is_setter) " +
                    "AND n.number_of_instructions > 0 " +
                    "RETURN percentileCont(n.number_of_instructions,0.25) as Q1, " +
                    "percentileCont(n.number_of_instructions,0.5) as MED," +
                    " percentileCont(n.number_of_instructions,0.75) as Q3";
            result = graphDatabaseService.execute(query);
            res = calculateThresholds(result);
        }
        writer.statsToCSV(res, "_STAT_NB_INSTRUCTIONS.csv");
    }

    private Map<String, Double> calculateThresholds(Result result) {
        Map<String, Double> res = new HashMap<>();
        // Only one result in that case
        while (result.hasNext()) {
            Map<String, Object> row = result.next();
            // Sometime neo4J returns a double or an int... with toString it works in all cases
            double q1 = Double.parseDouble(row.get("Q1").toString());
            double med = Double.parseDouble(row.get("MED").toString());
            double q3 = Double.parseDouble(row.get("Q3").toString());
            double high = q3 + (1.5 * (q3 - q1));
            double veryHigh = q3 + (3 * (q3 - q1));
            res.put("Q1", q1);
            res.put("Q3", q3);
            res.put("MED", med);
            res.put("HIGH (1.5)", high);
            res.put("VERY HIGH (3.0)", veryHigh);
        }
        return res;
    }

    /**
     * Excluding classes implementing 0 or 1 interface
     */
    public void calculateNumberOfImplementedInterfacesQuartile() throws IOException {
        Map<String, Double> res;
        Result result;
        try (Transaction ignored = graphDatabaseService.beginTx()) {
            String query = "MATCH (n:Class) WHERE n.number_of_implemented_interfaces > 1 " +
                    "RETURN percentileCont(n.number_of_implemented_interfaces,0.25) as Q1," +
                    " percentileCont(n.number_of_implemented_interfaces,0.5) as MED, " +
                    "percentileCont(n.number_of_implemented_interfaces,0.75) as Q3";
            result = graphDatabaseService.execute(query);
            res = calculateThresholds(result);
        }
        writer.statsToCSV(res, "_STAT_NB_INTERFACES.csv");
    }

    public void calculateNumberOfMethodsForInterfacesQuartile() throws IOException {
        Map<String, Double> res;
        Result result;
        try (Transaction ignored = graphDatabaseService.beginTx()) {
            String query = "MATCH (n:Class) WHERE exists(n.is_interface) " +
                    "RETURN percentileCont(n.number_of_methods,0.25) as Q1," +
                    " percentileCont(n.number_of_methods,0.5) as MED, " +
                    "percentileCont(n.number_of_methods,0.75) as Q3";
            result = graphDatabaseService.execute(query);
            res = calculateThresholds(result);
        }
        writer.statsToCSV(res, "_STAT_NB_METHODS_INTERFACE.csv");
    }

    public void calculateLackOfCohesionInMethodsQuartile() throws IOException {
        Map<String, Double> res;
        Result result;
        try (Transaction ignored = graphDatabaseService.beginTx()) {
            String query = "MATCH (n:Class) WHERE NOT exists(n.is_interface) AND NOT exists(n.is_abstract)" +
                    " RETURN percentileCont(n.lack_of_cohesion_in_methods,0.25) as Q1," +
                    " percentileCont(n.lack_of_cohesion_in_methods,0.5) as MED," +
                    " percentileCont(n.lack_of_cohesion_in_methods,0.75) as Q3";
            result = graphDatabaseService.execute(query);
            res = calculateThresholds(result);
        }
        writer.statsToCSV(res, "_STAT_LCOM.csv");
    }

    public void calculateNumberOfMethodsQuartile() throws IOException {
        Map<String, Double> res;
        Result result;
        try (Transaction ignored = graphDatabaseService.beginTx()) {
            String query = "MATCH (n:Class) WHERE NOT exists(n.is_interface) AND NOT exists(n.is_abstract) " +
                    "RETURN percentileCont(n.number_of_methods,0.25) as Q1, " +
                    "percentileCont(n.number_of_methods,0.5) as MED, " +
                    "percentileCont(n.number_of_methods,0.75) as Q3";
            result = graphDatabaseService.execute(query);
            res = calculateThresholds(result);
        }
        writer.statsToCSV(res, "_STAT_NB_METHODS.csv");
    }

    public void calculateNumberOfAttributesQuartile() throws IOException {
        Map<String, Double> res;
        Result result;
        try (Transaction ignored = graphDatabaseService.beginTx()) {
            String query = "MATCH (n:Class) WHERE NOT exists(n.is_interface) AND NOT exists(n.is_abstract) " +
                    "RETURN percentileCont(n.number_of_attributes,0.25) as Q1, " +
                    "percentileCont(n.number_of_attributes,0.5) as MED," +
                    " percentileCont(n.number_of_attributes,0.75) as Q3";
            result = graphDatabaseService.execute(query);
            res = calculateThresholds(result);
        }
        writer.statsToCSV(res, "_STAT_NB_ATTRIBUTES.csv");
    }
}
