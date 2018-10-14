package doridlens.query.commands;

import doridlens.query.neo4j.QueryEngine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 21:20
 * Description:
 */
public class ALLAPCommand implements PaprikaCommand {

    public static final String KEY = "ALLAP";

    private List<PaprikaCommand> commands;

    public ALLAPCommand(QueryEngine engine, PaprikaRequest fuzzy, PaprikaRequest nonFuzzy) {
        commands = new ArrayList<>();
//        commands.add(fuzzy.getCommand(engine));
        commands.add(nonFuzzy.getCommand(engine));
    }

    @Override
    public void run(boolean details) throws IOException {
        for (PaprikaCommand command : commands) {
            command.run(details);
        }
    }

}
