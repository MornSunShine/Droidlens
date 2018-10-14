package doridlens;

import doridlens.launcher.PaprikaStarter;
import doridlens.launcher.arg.PaprikaArgParser;
import doridlens.query.neo4j.QueryEngine;
import doridlens.query.neo4j.queries.QueryPropertiesException;

import java.io.IOException;
import java.io.PrintStream;

import static doridlens.launcher.arg.Argument.DELETE_KEY;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 19:53
 * Description: Delete模式启动器，删除Neo4J数据库中指定APK的数据
 */
public class DeleteModeStarter extends PaprikaStarter {

    private static final int BATCH_SIZE = 10000;

    public DeleteModeStarter(PaprikaArgParser argParser, PrintStream out) {
        super(argParser, out);
    }

    @Override
    public void start() {
        try {
            QueryEngine engine = createQueryEngine();
            String deleteQuery = "MATCH (n) WHERE n.app_key='" + argParser.getArg(DELETE_KEY) + "'\n" +
                    "WITH n LIMIT " + BATCH_SIZE + "\n" +
                    "DETACH DELETE n \n" +
                    "RETURN count(n)";
            out.println("Deleting nodes...");
            int count = 1;
            while (count != 0) {
                count = engine.executeAndCount(deleteQuery, "count(n)");
                printProgress();
            }
            out.println();
            out.println("Done.");
        } catch (IOException | QueryPropertiesException e) {
            e.printStackTrace(out);
        }
    }

    private int charCount = 1;

    private void printProgress() {
        if (charCount == 70) {
            out.println();
            charCount = 0;
        } else {
            out.print(".");
            charCount++;
        }
    }
}
