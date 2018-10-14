package doridlens.analyse.entities;

import static doridlens.analyse.entities.PaprikaApp.NO_SDK;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 20:29
 * Description: PaprikaAPP模型实体,包含PaprikaAPP的基本身份信息
 */
@SuppressWarnings("UnusedReturnValue")
public class PaprikaAppBuilder {

    private String name;
    private double rating;
    private String date;
    private String pack; //Package
    private int size;
    private String developer;
    private String category;
    private String price;
    private String key;
    private int nbDownload;
    private String versionCode;
    private String versionName;
    private int sdkVersion = NO_SDK;
    private int targetSdkVersion = NO_SDK;

    public String getName() {
        return name;
    }

    public PaprikaAppBuilder name(String name) {
        this.name = name;
        return this;
    }

    public PaprikaAppBuilder rating(double rating) {
        this.rating = rating;
        return this;
    }

    public PaprikaAppBuilder date(String date) {
        this.date = date;
        return this;
    }

    public PaprikaAppBuilder pack(String pack) {
        this.pack = pack;
        return this;
    }

    public PaprikaAppBuilder size(int size) {
        this.size = size;
        return this;
    }

    public PaprikaAppBuilder developer(String developer) {
        this.developer = developer;
        return this;
    }

    public PaprikaAppBuilder category(String category) {
        this.category = category;
        return this;
    }

    public PaprikaAppBuilder price(String price) {
        this.price = price;
        return this;
    }

    public PaprikaAppBuilder key(String key) {
        this.key = key;
        return this;
    }

    public PaprikaAppBuilder nbDownload(int nbDownload) {
        this.nbDownload = nbDownload;
        return this;
    }

    public PaprikaAppBuilder versionCode(String versionCode) {
        this.versionCode = versionCode;
        return this;
    }

    public PaprikaAppBuilder versionName(String versionName) {
        this.versionName = versionName;
        return this;
    }

    public PaprikaAppBuilder sdkVersion(int sdkVersion) {
        this.sdkVersion = sdkVersion;
        return this;
    }

    public PaprikaAppBuilder targetSdkVersion(int targetSdkVersion) {
        this.targetSdkVersion = targetSdkVersion;
        return this;
    }

    public boolean hasSDK() {
        return sdkVersion != NO_SDK;
    }

    public boolean hasTargetSDK() {
        return targetSdkVersion != NO_SDK;
    }

    public boolean hasPackage() {
        return !"".equals(pack);
    }

    public boolean hasName() {
        return !"".equals(name);
    }

    public PaprikaApp create() {
        return new PaprikaApp(name, key, pack, date, size, developer,
                category, price, rating, nbDownload, versionCode,
                versionName, sdkVersion, targetSdkVersion);
    }
}
