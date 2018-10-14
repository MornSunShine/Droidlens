package doridlens.query.commands;

import doridlens.query.neo4j.QuartileCalculator;
import doridlens.query.neo4j.QueryEngine;

import java.io.IOException;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 21:22
 * Description:
 */
public class StatsCommand implements PaprikaCommand {

    public static final String KEY = "STATS";

    private QueryEngine engine;

    public StatsCommand(QueryEngine engine) {
        this.engine = engine;
    }

    @Override
    public void run(boolean details) throws IOException {
        QuartileCalculator quartileCalculator = new QuartileCalculator(engine);
        quartileCalculator.calculateClassComplexityQuartile();
        quartileCalculator.calculateLackOfCohesionInMethodsQuartile();
        quartileCalculator.calculateNumberOfAttributesQuartile();
        quartileCalculator.calculateNumberOfImplementedInterfacesQuartile();
        quartileCalculator.calculateNumberOfMethodsQuartile();
        quartileCalculator.calculateNumberOfInstructionsQuartile();
        quartileCalculator.calculateCyclomaticComplexityQuartile();
        quartileCalculator.calculateNumberOfMethodsForInterfacesQuartile();
    }

}

