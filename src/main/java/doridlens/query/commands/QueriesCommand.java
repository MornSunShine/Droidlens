package doridlens.query.commands;

import doridlens.query.neo4j.QueryEngine;
import doridlens.query.neo4j.queries.PaprikaQuery;

import java.io.IOException;
import java.util.List;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 21:22
 * Description: 查询指令,负责Android Smell的查询
 */
public class QueriesCommand implements PaprikaCommand {

    private List<PaprikaQuery> queries;
    private QueryEngine engine;

    public QueriesCommand(QueryEngine engine, List<PaprikaQuery> queries) {
        this.queries = queries;
        this.engine = engine;
    }

    @Override
    public void run(boolean details) throws IOException {
        for (PaprikaQuery query : queries) {
            engine.execute(query, details);
        }
    }
}
