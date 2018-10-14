package doridlens.query.neo4j.queries;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 21:25
 * Description:
 */
public class QueryPropertiesException extends Exception {

    public QueryPropertiesException(String filename, String property, Throwable cause) {
        super("Found a threshold property \"" + property +
                        "\" that could not be casted to double in " +
                        (filename != null ?
                                "supplied properties file " + filename :
                                "default properties file"),
                cause);
    }
}
