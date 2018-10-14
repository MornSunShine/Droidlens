package doridlens.query.neo4j.queries.stats;

import doridlens.query.neo4j.queries.PaprikaQuery;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 22:15
 * Description:
 */
public class AnalyzedAppQuery extends PaprikaQuery {

    public static final String COMMAND_KEY = "ANALYZED";

    public AnalyzedAppQuery() {
        super(COMMAND_KEY);
    }

    /*
        MATCH (a:App)
        RETURN  a.app_key as app_key, a.category as category, a.package as package
            a.version_code as version_code, a.date_analysis as date_analysis,a.number_of_classes as number_of_classes,
            a.size as size,a.rating as rating,a.nb_download as nb_download, a.number_of_methods as number_of_methods,
            a.number_of_activities as number_of_activities,a.number_of_services as number_of_services,
            a.number_of_interfaces as number_of_interfaces,a.number_of_abstract_classes as number_of_abstract_classes,
            a.number_of_broadcast_receivers as number_of_broadcast_receivers,a.number_of_content_providers as number_of_content_providers,
            a.number_of_variables as number_of_variables, a.number_of_views as number_of_views,
            a.number_of_inner_classes as number_of_inner_classes, a.number_of_async_tasks as number_of_async_tasks
     */

    @Override
    public String getQuery(boolean details) {
        return "MATCH (a:App)\n" +
                "RETURN a.app_key as app_key," +
                "   a.category as category, " +
                "   a.package as package," +
                "   a.version_code as version_code, " +
                "   a.date_analysis as date_analysis," +
                "   a.number_of_classes as number_of_classes," +
                "   a.size as size," +
                "   a.rating as rating," +
                "   a.nb_download as nb_download, " +
                "   a.number_of_methods as number_of_methods," +
                "   a.number_of_activities as number_of_activities," +
                "   a.number_of_services as number_of_services," +
                "   a.number_of_interfaces as number_of_interfaces," +
                "   a.number_of_abstract_classes as number_of_abstract_classes," +
                "   a.number_of_broadcast_receivers as number_of_broadcast_receivers," +
                "   a.number_of_content_providers as number_of_content_providers," +
                "   a.number_of_variables as number_of_variables, " +
                "   a.number_of_views as number_of_views," +
                "   a.number_of_inner_classes as number_of_inner_classes," +
                "   a.number_of_async_tasks as number_of_async_tasks";
    }

}