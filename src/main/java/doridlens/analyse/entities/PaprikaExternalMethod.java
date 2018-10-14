package doridlens.analyse.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 20:33
 * Description: Paprika外部方法
 */
public class PaprikaExternalMethod extends Entity {

    // Neo4J attributes names
    public static final String N4J_APP_KEY = PaprikaApp.N4J_APP_KEY;
    public static final String N4J_NAME = "name";
    public static final String N4J_FULL_NAME = "full_name";
    public static final String N4J_RETURN_TYPE = "return_type";

    private PaprikaExternalClass paprikaExternalClass;
    private List<PaprikaExternalArgument> paprikaExternalArguments;
    private String returnType;

    public String getReturnType() {
        return returnType;
    }

    public List<PaprikaExternalArgument> getPaprikaExternalArguments() {
        return paprikaExternalArguments;
    }

    private PaprikaExternalMethod(String name, String returnType,
                                  PaprikaExternalClass paprikaExternalClass) {
        this.setName(name);
        this.paprikaExternalClass = paprikaExternalClass;
        this.returnType = returnType;
        this.paprikaExternalArguments = new ArrayList<>();
    }

    public static PaprikaExternalMethod create(String name, String returnType, PaprikaExternalClass paprikaClass) {
        PaprikaExternalMethod paprikaMethod = new PaprikaExternalMethod(name, returnType, paprikaClass);
        paprikaClass.addPaprikaExternalMethod(paprikaMethod);
        return paprikaMethod;
    }

    public PaprikaExternalClass getPaprikaExternalClass() {
        return paprikaExternalClass;
    }

    public void setPaprikaExternalClass(PaprikaExternalClass paprikaClass) {
        this.paprikaExternalClass = paprikaClass;
    }

    @Override
    public String toString() {
        return this.getName() + "#" + paprikaExternalClass;
    }

    public void addExternalArgument(PaprikaExternalArgument paprikaExternalArgument) {
        this.paprikaExternalArguments.add(paprikaExternalArgument);
    }
}
