package doridlens.analyse.entities;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 20:36
 * Description: 变量实体类,变量相关的详细信息
 */
public class PaprikaVariable extends Entity {

    private String type;
    private PaprikaModifier modifier;

    // Neo4J attributes names
    public static final String N4J_APP_KEY = PaprikaApp.N4J_APP_KEY;
    public static final String N4J_NAME = "name";
    public static final String N4J_MODIFIER = "modifier";
    public static final String N4J_TYPE = "type";

    public String getType() {
        return type;
    }

    private PaprikaVariable(String name, String type, PaprikaModifier modifier) {
        this.type = type;
        this.name = name;
        this.modifier = modifier;
    }

    public static PaprikaVariable create(String name, String type, PaprikaModifier modifier,
                                         PaprikaClass paprikaClass) {
        PaprikaVariable paprikaVariable = new PaprikaVariable(name, type, modifier);
        paprikaClass.addPaprikaVariable(paprikaVariable);
        return paprikaVariable;
    }

    public String getModifierAsString() {
        return modifier.getName();
    }

    public boolean isPublic() {
        return modifier == PaprikaModifier.PUBLIC;
    }

    public boolean isPrivate() {
        return modifier == PaprikaModifier.PRIVATE;
    }

    public boolean isProtected() {
        return modifier == PaprikaModifier.PROTECTED;
    }
}
