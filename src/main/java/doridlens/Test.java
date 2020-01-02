package doridlens;

import doridlens.neo4j.QueryEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

import java.io.IOException;

/**
 * Author: MaoMorn
 * Date: 2020/1/2
 * Time: 9:49
 * Description:
 */
public class Test {
    public static void main(String[] args) {
        String dbPath = "D:\\DataBase\\Neo4j CE 3.3.1\\data\\K-9 Mail_5.600.db";
        QueryEngine engine = new QueryEngine(dbPath);
        GraphDatabaseService sevice = engine.getGraphDatabaseService();
        try (Transaction ignored = sevice.beginTx()) {
            String query = "MATCH (c:Class) RETURN c.name as name,EXISTS(c.is_inner_class) as is_inner_class," +
                    "EXISTS(c.is_static) as is_static,EXISTS(c.is_interface) as is_interface,EXISTS(c.is_activity) as is_activity";
            String query1 = "MATCH (c:Class) WHERE EXISTS(c.is_inner_class) AND NOT EXISTS(c.is_static) RETURN c.name as name,EXISTS(c.is_inner_class) as is_inner_class," +
                    "EXISTS(c.is_static) as is_static,EXISTS(c.is_interface) as is_interface,EXISTS(c.is_activity) as is_activity";
            String query2 = "MATCH (c:Class) RETURN c.number_of_methods,c.depth_of_inheritance,c.number_of_implemented_interfaces,c.number_of_attributes," +
                    "c.number_of_children,c.class_complexity,c.coupling_between_objects,c.lack_of_cohesion_in_methods, CASE EXISTS(c.is_inner_class) WHEN TRUE THEN 1 ELSE 0 END as is_inner_class,CASE EXISTS(c.is_static) WHEN TRUE THEN 1 ELSE 0 END as is_static,CASE EXISTS(c.is_inner_class) AND NOT EXISTS(c.is_static) WHEN TRUE THEN 1 ELSE 0 END as result";
            Result result = sevice.execute(query2);
            engine.resultToCSV(result, "data.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
