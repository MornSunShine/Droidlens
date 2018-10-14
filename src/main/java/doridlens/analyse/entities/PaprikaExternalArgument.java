package doridlens.analyse.entities;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 20:30
 * Description: Paprika外部参数
 */
public class PaprikaExternalArgument extends Entity {

    // Neo4J attributes names
    public static final String N4J_APP_KEY = PaprikaApp.N4J_APP_KEY;
    public static final String N4J_NAME = "name";
    public static final String N4J_POSITION = "position";

    private int position;

    private PaprikaExternalArgument(String name, int position) {
        this.name = name;
        this.position = position;
    }

    public static PaprikaExternalArgument create(String name, int position, PaprikaExternalMethod paprikaExternalMethod) {
        PaprikaExternalArgument paprikaExternalArgument = new PaprikaExternalArgument(name, position);
        paprikaExternalMethod.addExternalArgument(paprikaExternalArgument);
        return paprikaExternalArgument;
    }

    public int getPosition() {
        return position;
    }

}