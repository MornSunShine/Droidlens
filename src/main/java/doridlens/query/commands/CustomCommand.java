package doridlens.query.commands;

import doridlens.query.neo4j.QueryEngine;

import java.io.IOException;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 21:20
 * Description: 自定义指令
 */
public class CustomCommand implements PaprikaCommand {

    private String request;
    private QueryEngine engine;

    public CustomCommand(QueryEngine engine, String request) {
        this.request = request;
        this.engine = engine;
    }

    @Override
    public void run(boolean details) throws IOException {
        engine.executeAndWriteToCSV(request, "_CUSTOM.csv");
    }

}

