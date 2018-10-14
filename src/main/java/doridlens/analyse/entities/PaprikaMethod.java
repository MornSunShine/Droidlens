package doridlens.analyse.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 20:36
 * Description: 成员函数实体类,成员函数相关的详细信息
 */
public class PaprikaMethod extends Entity {

    // Neo4J attributes names
    public static final String N4J_APP_KEY = PaprikaApp.N4J_APP_KEY;
    public static final String N4J_NAME = "name";
    public static final String N4J_MODIFIER = "modifier";
    public static final String N4J_FULL_NAME = "full_name";
    public static final String N4J_RETURN_TYPE = "return_type";

    private PaprikaClass paprikaClass;
    private String returnType;
    private Set<PaprikaVariable> usedVariables;
    private Set<Entity> calledMethods;
    private PaprikaModifier modifier;
    private List<PaprikaArgument> arguments;

    private PaprikaMethod(String name, PaprikaModifier modifier, String returnType, PaprikaClass paprikaClass) {
        this.setName(name);
        this.paprikaClass = paprikaClass;
        this.usedVariables = new HashSet<>(0);
        this.calledMethods = new HashSet<>(0);
        this.arguments = new ArrayList<>(0);
        this.modifier = modifier;
        this.returnType = returnType;
    }


    public static PaprikaMethod create(String name, PaprikaModifier modifier, String returnType,
                                       PaprikaClass paprikaClass) {
        PaprikaMethod paprikaMethod = new PaprikaMethod(name, modifier, returnType, paprikaClass);
        paprikaClass.addPaprikaMethod(paprikaMethod);
        return paprikaMethod;
    }

    public String getReturnType() {
        return returnType;
    }

    public String getModifierAsString() {
        return modifier.getName();
    }

    public PaprikaClass getPaprikaClass() {
        return paprikaClass;
    }

    public void setPaprikaClass(PaprikaClass paprikaClass) {
        this.paprikaClass = paprikaClass;
    }

    @Override
    public String toString() {
        return this.getName() + "#" + paprikaClass;
    }

    public void useVariable(PaprikaVariable paprikaVariable) {
        usedVariables.add(paprikaVariable);
    }

    public Set<PaprikaVariable> getUsedVariables() {
        return this.usedVariables;
    }

    public void callMethod(Entity paprikaMethod) {
        calledMethods.add(paprikaMethod);
    }

    public Set<Entity> getCalledMethods() {
        return this.calledMethods;
    }

    public boolean haveCommonFields(PaprikaMethod paprikaMethod) {
        Set<PaprikaVariable> otherVariables = paprikaMethod.getUsedVariables();
        for (PaprikaVariable paprikaVariable : usedVariables) {
            if (otherVariables.contains(paprikaVariable)) return true;
        }
        return false;
    }

    public void addArgument(PaprikaArgument paprikaArgument) {
        this.arguments.add(paprikaArgument);
    }

    public List<PaprikaArgument> getArguments() {
        return arguments;
    }
}
