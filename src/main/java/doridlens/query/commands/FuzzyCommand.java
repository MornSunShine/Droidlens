package doridlens.query.commands;

import doridlens.query.neo4j.QueryEngine;
import doridlens.query.neo4j.queries.antipatterns.fuzzy.FuzzyQuery;

import java.io.IOException;
import java.util.List;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 21:20
 * Description:
 */
public class FuzzyCommand implements PaprikaCommand {

    private QueryEngine engine;
    private List<FuzzyQuery> queries;

    public FuzzyCommand(QueryEngine engine, List<FuzzyQuery> queries) {
        this.engine = engine;
        this.queries = queries;
    }

    @Override
    public void run(boolean details) throws IOException {
        for (FuzzyQuery query : queries) {
            engine.executeFuzzyAndWriteToCSV(query, details);
        }
    }

}
