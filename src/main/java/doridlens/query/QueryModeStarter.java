package doridlens.query;

import doridlens.launcher.PaprikaStarter;
import doridlens.launcher.arg.PaprikaArgParser;
import doridlens.query.commands.CustomCommand;
import doridlens.query.commands.PaprikaRequest;
import doridlens.query.neo4j.QueryEngine;
import doridlens.query.neo4j.queries.QueryPropertiesException;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static doridlens.launcher.arg.Argument.*;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 21:19
 * Description: Query模式启动器，从Neo4j数据库中统计Smells信息
 */
public class QueryModeStarter extends PaprikaStarter {

    public QueryModeStarter(PaprikaArgParser argParser, PrintStream out) {
        super(argParser, out);
    }

    @Override
    public void start() {
        try {
            if (!new File(argParser.getArg(DATABASE_ARG)).exists()) {
                out.println("No database was found on the given path.");
            }
            out.println("Executing Queries");
            QueryEngine queryEngine = createQueryEngine();
            String request = argParser.getArg(REQUEST_ARG);
            Boolean details = argParser.getFlagArg(DETAILS_ARG);
            String csvPrefix = getCSVPrefix(argParser.getArg(CSV_ARG));
            out.println("Resulting csv file name will start with prefix " + csvPrefix);
            queryEngine.setCsvPrefix(csvPrefix);
            PaprikaRequest paprikaRequest = PaprikaRequest.getRequest(request);
            if (paprikaRequest != null) {
                paprikaRequest.getCommand(queryEngine).run(details);
            } else {
                out.println("Executing custom request");
                new CustomCommand(queryEngine, request).run(false);
            }
            queryEngine.shutDown();
            out.println("Done");
        } catch (IOException | QueryPropertiesException e) {
            e.printStackTrace(out);
        }
    }

    /**
     * CSV输出文件前缀,年_月_日_时_分
     *
     * @param csvPath CSV文件的路径
     * @return 添加日期前缀后的CSV文件路径
     */
    private String getCSVPrefix(String csvPath) {
        Calendar cal = new GregorianCalendar();
        String csvDate = cal.get(Calendar.YEAR) + "_" + cal.get(Calendar.MONTH) + 1
                + "_" + cal.get(Calendar.DAY_OF_MONTH) + "_" + cal.get(Calendar.HOUR_OF_DAY)
                + "_" + cal.get(Calendar.MINUTE);
        return csvPath + "\\" + csvDate;
    }

}
