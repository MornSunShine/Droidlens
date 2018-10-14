package doridlens.analyse.entities;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 20:29
 * Description: 参数实体类
 */
public class PaprikaArgument extends Entity {

    // Neo4J attributes names
    public static final String N4J_APP_KEY = PaprikaApp.N4J_APP_KEY;
    public static final String N4J_NAME = "name";
    public static final String N4J_POSITION = "position";

    private int position;

    private PaprikaArgument(String name, int position) {
        this.name = name;
        this.position = position;
    }

    @SuppressWarnings("UnusedReturnValue")
    public static PaprikaArgument create(String name, int position, PaprikaMethod paprikaMethod) {
        PaprikaArgument paprikaArgument = new PaprikaArgument(name, position);
        paprikaMethod.addArgument(paprikaArgument);
        return paprikaArgument;
    }

    public int getPosition() {
        return position;
    }
}
