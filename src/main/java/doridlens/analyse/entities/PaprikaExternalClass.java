package doridlens.analyse.entities;

import java.util.HashSet;
import java.util.Set;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 20:33
 * Description: Paprika外部类
 */
public class PaprikaExternalClass extends Entity {

    // Neo4J attributes names
    public static final String N4J_APP_KEY = PaprikaApp.N4J_APP_KEY;
    public static final String N4J_NAME = "name";
    public static final String N4J_PARENT = "parent_name";

    private PaprikaApp paprikaApp;
    private String parentName;
    private Set<PaprikaExternalMethod> paprikaExternalMethods;

    public Set<PaprikaExternalMethod> getPaprikaExternalMethods() {
        return paprikaExternalMethods;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    private PaprikaExternalClass(String name, PaprikaApp paprikaApp) {
        this.setName(name);
        this.paprikaApp = paprikaApp;
        this.paprikaExternalMethods = new HashSet<>();
    }

    public static PaprikaExternalClass create(String name, PaprikaApp paprikaApp) {
        PaprikaExternalClass paprikaClass = new PaprikaExternalClass(name, paprikaApp);
        paprikaApp.addPaprikaExternalClass(paprikaClass);
        return paprikaClass;
    }

    public void addPaprikaExternalMethod(PaprikaExternalMethod paprikaMethod) {
        paprikaExternalMethods.add(paprikaMethod);
    }

    public PaprikaApp getPaprikaApp() {
        return paprikaApp;
    }

    public void setPaprikaApp(PaprikaApp paprikaApp) {
        this.paprikaApp = paprikaApp;
    }

}
