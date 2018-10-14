package doridlens.query.neo4j;

import doridlens.DatabaseManager;
import doridlens.analyse.entities.PaprikaApp;
import doridlens.launcher.arg.PaprikaArgParser;
import doridlens.query.neo4j.queries.PaprikaQuery;
import doridlens.query.neo4j.queries.QueryPropertiesReader;
import doridlens.query.neo4j.queries.antipatterns.fuzzy.FuzzyQuery;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static doridlens.launcher.arg.Argument.DATABASE_ARG;
import static doridlens.launcher.arg.Argument.NO_NAMES_ARG;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 21:23
 * Description: 查询引擎,对Neo4J负责,执行具体的查询操作
 */
public class QueryEngine {

    private static final String APP_NAMES_QUERY =
            "MATCH (n:App) RETURN n.app_key AS app_key, n.name AS app_name";

    private static final String APP_NAME_COLUMN = "app_name";

    private Map<String, String> keysToNames;

    private GraphDatabaseService graphDatabaseService;
    private DatabaseManager databaseManager;
    private PaprikaArgParser arg;
    private String csvPrefix;
    private QueryPropertiesReader propsReader;

    public QueryEngine(PaprikaArgParser arg, QueryPropertiesReader reader) {
        this.databaseManager = new DatabaseManager(arg.getArg(DATABASE_ARG));
        this.arg = arg;
        this.propsReader = reader;
        databaseManager.start();
        graphDatabaseService = databaseManager.getGraphDatabaseService();
        csvPrefix = "";
    }

    public String getCsvPrefix() {
        return csvPrefix;
    }

    public void setCsvPrefix(String csvPrefix) {
        this.csvPrefix = csvPrefix;
    }

    public GraphDatabaseService getGraphDatabaseService() {
        return graphDatabaseService;
    }

    public QueryPropertiesReader getPropsReader() {
        return propsReader;
    }

    /**
     * 执行查询操作
     *
     * @param query   查询命令
     * @param details 是否添加详情到AntiPattern
     * @throws IOException 读取文件失败,则抛出
     */
    public void execute(PaprikaQuery query, boolean details) throws IOException {
        executeAndWriteToCSV(query.getQuery(details), query.getCSVSuffix());
    }

    /**
     * 执行查询并写入结果到CSV
     *
     * @param request Cypher数据库查询命令
     * @param suffix  CSV文件后缀
     * @throws IOException 读取文件失败,则抛出
     */
    public void executeAndWriteToCSV(String request, String suffix)
            throws IOException {
        try (Transaction ignored = graphDatabaseService.beginTx()) {
            Result result = graphDatabaseService.execute(request);
            List<Map<String, Object>> rows = result.stream().map(HashMap::new).collect(Collectors.toList());
            List<String> columns = new ArrayList<>(result.columns());
            if (!arg.getFlagArg(NO_NAMES_ARG)) {
                addAppNamesToResult(rows, columns);
            }
            new CSVWriter(csvPrefix).resultToCSV(rows, columns, suffix);
        }
    }

    /**
     * 执行模糊查询,并写入结果到CSV
     *
     * @param query   Fuzzy查询Command
     * @param details 是否在Query语句中要求详情
     * @throws IOException 读取文件失败,则抛出异常
     */
    public void executeFuzzyAndWriteToCSV(FuzzyQuery query, boolean details) throws IOException {
        try (Transaction ignored = graphDatabaseService.beginTx()) {
            Result result = graphDatabaseService.execute(query.getFuzzyQuery(details));
            List<Map<String, Object>> rows = result.stream().map(HashMap::new).collect(Collectors.toList());
            List<String> columns = new ArrayList<>(result.columns());
            if (!arg.getFlagArg(NO_NAMES_ARG)) {
                addAppNamesToResult(rows, columns);
            }
            columns.add("fuzzy_value");
            new CSVWriter(csvPrefix).resultToCSV(query.getFuzzyResult(rows, query.getFcl()),
                    columns, query.getFuzzySuffix());
        }
    }

    /**
     * 添加应用名称到结果
     *
     * @param rows    数据行列表
     * @param columns 列名列表
     */
    private void addAppNamesToResult(List<Map<String, Object>> rows, List<String> columns) {
        if (rows.isEmpty() || rows.get(0).get(PaprikaApp.N4J_APP_KEY) == null) {
            return;
        }
        columns.add(APP_NAME_COLUMN);
        if (keysToNames == null) {
            fillKeysToNames();
        }
        rows.forEach(row -> row.put(APP_NAME_COLUMN, keysToNames.get(row.get(PaprikaApp.N4J_APP_KEY).toString())));
    }

    /**
     * 应用的Key与名称进行对应
     */
    private void fillKeysToNames() {
        try (Transaction ignored = graphDatabaseService.beginTx()) {
            Result namesResult = graphDatabaseService.execute(APP_NAMES_QUERY);
            keysToNames = namesResultToMap(namesResult.stream()
                    .collect(Collectors.toList()));
        }
    }

    /**
     * app_key,app_name信息处理
     *
     * @param rows app_key与app_name信息列表
     * @return 处理后得到的key-name键值对
     */
    private Map<String, String> namesResultToMap(List<Map<String, Object>> rows) {
        Map<String, String> result = new HashMap<>();
        rows.forEach(row -> result.put(row.get(PaprikaApp.N4J_APP_KEY).toString(), row.get(APP_NAME_COLUMN).toString()));
        return result;
    }

    /**
     * 查询执行计数,一般用于Delete Mode
     *
     * @param request    Cypher指令
     * @param countLabel 计数命令
     * @return 计数结果
     */
    public int executeAndCount(String request, String countLabel) {
        try (Transaction transaction = graphDatabaseService.beginTx()) {
            Result result = graphDatabaseService.execute(request);
            transaction.success();
            return Integer.valueOf(result.next().get(countLabel).toString());
        }
    }

    public void shutDown() {
        databaseManager.shutDown();
    }
}