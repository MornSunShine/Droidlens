package doridlens.analyse.entities;

import soot.SootClass;
import soot.SootField;
import soot.SootMethod;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 20:36
 * Description: 访问控制修饰词
 */
public enum PaprikaModifier {

    PRIVATE("private"),
    PROTECTED("protected"),
    PUBLIC("public");

    private String name;

    PaprikaModifier(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static PaprikaModifier getModifier(SootClass sootClass) {
        return chooseModifier(sootClass.isPublic(), sootClass.isProtected());
    }

    public static PaprikaModifier getModifier(SootField sootField) {
        return chooseModifier(sootField.isPublic(), sootField.isProtected());
    }

    public static PaprikaModifier getModifier(SootMethod sootMethod) {
        return chooseModifier(sootMethod.isPublic(), sootMethod.isProtected());
    }

    private static PaprikaModifier chooseModifier(boolean isPublic, boolean isProtected) {
        if (isPublic) return PUBLIC;
        if (isProtected) return PROTECTED;
        return PRIVATE;
    }
}
