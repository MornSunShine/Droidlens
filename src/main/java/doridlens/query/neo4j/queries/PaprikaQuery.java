package doridlens.query.neo4j.queries;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 21:25
 * Description: Neo4J查询抽象类
 *              方便Smell查询的管理,Smell通过实现该类进行具体查询操作
 */
public abstract class PaprikaQuery {

    protected String queryName;

    public PaprikaQuery(String queryName) {
        this.queryName = queryName;
    }

    public abstract String getQuery(boolean details);

    public String getCSVSuffix() {
        return "_" + queryName + ".csv";
    }

}
