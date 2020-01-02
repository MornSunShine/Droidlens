package doridlens.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: MaoMorn
 * Date: 2020/1/2
 * Time: 9:19
 * Description:
 */
public class PaprikaApp extends Entity {
    private String pack; //Package
    private String sdkVersion;
    private String targetSdkVersion;
    private List<PaprikaClass> paprikaClasses;
    private List<PaprikaExternalClass> paprikaExternalClasses;

    private PaprikaApp(String name, String pack, String sdkVersion, String targetSdkVersion) {
        this.name = name;
        this.pack = pack;
        this.paprikaClasses = new ArrayList<>();
        this.paprikaExternalClasses = new ArrayList<>();
        this.sdkVersion = sdkVersion;
        this.targetSdkVersion = targetSdkVersion;
    }


    public List<PaprikaExternalClass> getPaprikaExternalClasses() {
        return paprikaExternalClasses;
    }


    public void addPaprikaExternalClass(PaprikaExternalClass paprikaExternalClass) {
        paprikaExternalClasses.add(paprikaExternalClass);
    }

    public List<PaprikaClass> getPaprikaClasses() {
        return paprikaClasses;
    }


    public void addPaprikaClass(PaprikaClass paprikaClass) {
        paprikaClasses.add(paprikaClass);
    }

    public static PaprikaApp createPaprikaApp(String name, String pack, String sdkVersion, String targetSdkVersion) {
        return new PaprikaApp(name, pack, sdkVersion, targetSdkVersion);
    }

    public String getPack() {
        return pack;
    }

    public String getSdkVersion() {
        return sdkVersion;
    }

    public String getTargetSdkVersion() {
        return targetSdkVersion;
    }
}
