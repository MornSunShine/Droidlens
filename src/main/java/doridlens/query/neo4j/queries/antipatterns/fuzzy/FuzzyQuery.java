package doridlens.query.neo4j.queries.antipatterns.fuzzy;

import doridlens.query.neo4j.queries.PaprikaQuery;
import doridlens.query.neo4j.queries.QueryPropertiesReader;
import net.sourceforge.jFuzzyLogic.FIS;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 22:25
 * Description:
 */
public abstract class FuzzyQuery extends PaprikaQuery {

    private static final String fclFolder = "/fcl/";

    private String fclFile;

    protected QueryPropertiesReader reader;

    public FuzzyQuery(String name, String fclFile, QueryPropertiesReader reader) {
        super(name);
        this.fclFile = fclFile;
        this.reader = reader;
    }

    public FIS getFcl() throws FileNotFoundException {
        File fcf = new File(fclFile);
        // We look if the file is in a directory otherwise we look inside the jar
        if (fcf.exists() && !fcf.isDirectory()) {
            return FIS.load(injectProperties(new FileInputStream(fclFile)), false);
        } else {
            return FIS.load(injectProperties(getClass().getResourceAsStream(fclFolder + fclFile)), false);
        }
    }

    private InputStream injectProperties(InputStream original) {
        String function = new BufferedReader(new InputStreamReader(original))
                .lines().collect(Collectors.joining("\n"));
        function = reader.replaceProperties(function);
        return new ByteArrayInputStream(function.getBytes());
    }

    public abstract String getFuzzyQuery(boolean details);

    public abstract List<Map<String, Object>> getFuzzyResult(List<Map<String, Object>> result, FIS fis);

    public String getFuzzySuffix() {
        return super.getCSVSuffix();
    }

    @Override
    public String getCSVSuffix() {
        return "_" + queryName + "_NO_FUZZY.csv";
    }

}