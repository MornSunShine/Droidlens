package doridlens;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 19:54
 * Description: Neo4j数据库管理类
 */
public class DatabaseManager {

    private final String dbPath;
    private GraphDatabaseService graphDatabaseService;

    public DatabaseManager(String dbPath) {
        this.dbPath = dbPath;
    }

    /**
     * 防数据库异常关闭挂钩
     * @param graphDb 数据库服务实例
     */
    private static void registerShutdownHook(final GraphDatabaseService graphDb) {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook(new Thread(() -> graphDb.shutdown()));
    }

    /**
     * 启动数据库
     */
    public void start() {
        File dbFile = new File(dbPath);
        graphDatabaseService = new GraphDatabaseFactory()
                .newEmbeddedDatabaseBuilder(dbFile)
                .newGraphDatabase();
        registerShutdownHook(graphDatabaseService);
    }

    /**
     * 关闭数据库
     */
    public void shutDown() {
        graphDatabaseService.shutdown();
    }

    public GraphDatabaseService getGraphDatabaseService() {
        return graphDatabaseService;
    }
}
