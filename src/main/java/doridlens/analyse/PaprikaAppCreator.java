package doridlens.analyse;

import doridlens.analyse.entities.PaprikaApp;
import doridlens.analyse.entities.PaprikaAppBuilder;
import doridlens.launcher.arg.Argument;
import doridlens.launcher.arg.PaprikaArgParser;
import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.ApkMeta;
import soot.Scene;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static doridlens.launcher.arg.Argument.*;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 19:57
 * Description: 提取资源新信息创建PaprikaAPP模型
 *              信息来源包括:
 *              - 提供给Paprika的参数
 *              - JSON属性文件
 *              - 应用文件名和AndroidManifest.xml
 */
public class PaprikaAppCreator {

    private PaprikaAppBuilder builder;
    private String apkPath;
    private PaprikaArgParser argParser;

    /**
     * 构造函数
     *
     * @param argParser Paprika参数解析器
     * @param apkPath   APK路径
     */
    public PaprikaAppCreator(PaprikaArgParser argParser, String apkPath) {
        this.apkPath = apkPath;
        this.argParser = argParser;
        this.builder = new PaprikaAppBuilder();
    }

    /**
     * 从CLI参数读取应用属性,计算SHA-256作为标识KEY
     *
     * @throws IOException              读取APK失败,则抛出异常
     * @throws NoSuchAlgorithmException 计算SHA-256失败,则抛出异常
     */
    public void readAppInfo() throws IOException, NoSuchAlgorithmException {
        builder.name(argParser.getArg(NAME_ARG))
                .pack(argParser.getArg(PACKAGE_ARG))
                .date(argParser.getArg(DATE_ARG))
                .size(argParser.getIntArg(SIZE_ARG))
                .developer(argParser.getArg(DEVELOPER_ARG))
                .category(argParser.getArg(CATEGORY_ARG))
                .price(argParser.getArg(PRICE_ARG))
                .rating(argParser.getDoubleArg(RATING_ARG))
                .nbDownload(argParser.getIntArg(NB_DOWNLOAD_ARG))
                .versionCode(argParser.getArg(VERSION_CODE_ARG))
                .versionName(argParser.getArg(VERSION_NAME_ARG))
                .sdkVersion(argParser.getIntArg(SDK_VERSION_ARG))
                .targetSdkVersion(argParser.getIntArg(TARGET_SDK_VERSION_ARG));
        if (!argParser.isFolderMode()) {
            builder.key(argParser.getSha());
        } else {
            builder.name("");
            builder.key(argParser.computeSha256(apkPath));
            builder.pack("");
        }
    }

    /**
     * 通过解析应用文件,比如AndroidManifest.xml,补充{@link #readAppInfo()}未载入的信息
     * 包括:
     * - 目标SDK, AndroidManifest.xml
     * - 应用的主包, AndroidManifest.xml
     * - 应用名, 默认为APK文件名称
     * <p>
     * 仅当Soot获得APK路径后,才能运行该方法 has been given the apk path.
     *
     * @throws IOException APK或AndroidManifest.xml文件读取失败,则抛出异常
     */
    public void fetchMissingAppInfo() throws IOException {
        if (!builder.hasTargetSDK()) {
            builder.targetSdkVersion(Scene.v().getAndroidAPIVersion());
        }
        if (!builder.hasPackage() || !builder.hasSDK()) {
            try (ApkFile apkFile = new ApkFile(new File(apkPath))) {
                ApkMeta apkMeta = apkFile.getApkMeta();
                if (!builder.hasPackage()) {
                    builder.pack(apkMeta.getPackageName());
                }
                if (!builder.hasSDK()) {
                    builder.sdkVersion(Integer.parseInt(apkMeta.getMinSdkVersion()));
                }
            }
        }
        if (!builder.hasName()) {
            String filename = new File(apkPath).getName();
            if (filename.endsWith(".apk")) {
                filename = filename.substring(0, filename.length() - 4);
            }
            builder.name(filename);
        }
    }

    /**
     * 添加从JSON属性文件读取的APP属性,若已存在,则覆盖存在的属性值
     * <p>
     * 仅当属性值已被载入时才执行该方法
     */
    public void addApkProperties(ApkPropertiesParser propsParser) {
        if (!propsParser.hasProperties(builder.getName())) {
            return;
        }
        for (Argument arg : Argument.ANALYSE_PROPS_ARGS) {
            arg.insertAppProperty(propsParser, builder);
        }
        // Name has to be last since the field is used to fetch the other properties
        Argument.NAME_ARG.insertAppProperty(propsParser, builder);
    }

    /**
     * 创建PaprikaAPP模型
     *
     * @return PaprikaAPP模型实例
     */
    public PaprikaApp createApp() {
        return builder.create();
    }

}
