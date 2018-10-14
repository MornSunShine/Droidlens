package doridlens.launcher.arg;

import doridlens.launcher.PaprikaMode;
import doridlens.launcher.PaprikaStarter;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparsers;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static doridlens.launcher.PaprikaMode.ANALYSE_MODE;
import static doridlens.launcher.arg.Argument.*;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 21:18
 * Description: Paprika命令行解析器
 */
public class PaprikaArgParser {

    private static final String SUB_PARSER = "sub_command";
    private static final String DATE_REGEX =
            "^([0-9]{4})-([0-1][0-9])-([0-3][0-9])\\s([0-1][0-9]|[2][0-3]):([0-5][0-9]):([0-5][0-9]).([0-9]*)$";

    private ArgumentParser parser;
    private Namespace res;

    public PaprikaArgParser() {
        parser = ArgumentParsers.newFor("paprika").build();
        Subparsers subparsers = parser.addSubparsers().dest(SUB_PARSER);
        for (PaprikaMode mode : PaprikaMode.values()) {
            mode.setupAllArgs(subparsers);
        }
    }

    /**
     * 解析Paprika参数
     *
     * @param args 待解析命令行
     * @throws ArgumentParserException 参数不匹配，则抛出异常
     * @throws PaprikaArgException     日期格式不匹配，则抛出异常
     */
    public void parseArgs(String[] args) throws ArgumentParserException, PaprikaArgException {
        res = parser.parseArgs(args);
        if (isAnalyseMode() && res.get(UNSAFE_ARG.toString()) == null) {
            checkArgs();
        }
    }

    private boolean isAnalyseMode() {
        return res.getString(SUB_PARSER).equals(ANALYSE_MODE.toString());
    }

    private void checkArgs() throws PaprikaArgException {
        if (!res.getString(DATE_ARG.toString()).matches(DATE_REGEX)) {
            throw new PaprikaArgException("Date should be formatted : yyyy-mm-dd hh:mm:ss.S");
        }
    }

    /**
     * 计算文件的SHA-256
     *
     * @param path 待计算文件路径
     * @return 文件的SHA-256
     * @throws IOException              文件查找读取失败，则抛出异常
     * @throws NoSuchAlgorithmException SHA-256计算方法未找到，则抛出异常
     */
    public String computeSha256(String path) throws IOException, NoSuchAlgorithmException {
        byte[] buffer = new byte[2048];
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        try (InputStream is = new FileInputStream(path)) {
            while (true) {
                int readBytes = is.read(buffer);
                if (readBytes > 0)
                    digest.update(buffer, 0, readBytes);
                else
                    break;
            }
        }
        byte[] hashValue = digest.digest();
        StringBuilder sb = new StringBuilder();
        for (byte aHashValue : hashValue) {
            sb.append(Integer.toString((aHashValue & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    /**
     * 获取与{@link #parseArgs(String[])}载入的参数匹配的Paprika启动器实例
     *
     * @param out 反馈输出口
     * @return Paprika会话启动器实例
     */
    public PaprikaStarter getSelectedStarter(PrintStream out) {
        PaprikaMode selectedMode = PaprikaMode.getMode(res.getString(SUB_PARSER));
        if (selectedMode == null) {
            out.println(res.getString(SUB_PARSER) + " is not a valid Paprika argument.");
            return null;
        }
        return selectedMode.getStarter(this, out);
    }

    public void handleError(ArgumentParserException e) {
        // All subparsers error handlers are the same
        ANALYSE_MODE.getSubParser().handleError(e);
    }

    public String getArg(Argument arg) {
        return res.getString(arg.toString());
    }

    public int getIntArg(Argument arg) {
        return res.getInt(arg.toString());
    }

    public double getDoubleArg(Argument arg) {
        return res.getDouble(arg.toString());
    }

    public boolean getFlagArg(Argument arg) {
        return res.getBoolean(arg.toString());
    }

    /**
     * 获取分析单个APK时使用的SHA-256摘要
     *
     * @return 参数--key的值,如果这个参数没有被使用,则返回根据APK内容计算的SHA-256
     * @throws IOException              读取或查找文件失败,则抛出异常
     * @throws NoSuchAlgorithmException SHA-256算法找不到,则抛出异常
     */
    public String getSha() throws IOException, NoSuchAlgorithmException {
        if (res.getString(KEY_ARG.toString()) == null) {
            return computeSha256(res.getString(APK_ARG.toString()));
        } else {
            return res.getString(KEY_ARG.toString());
        }
    }

    /**
     * 返回参数APK_ARG对应目录下所有APK的路径
     */
    public List<String> getAppsPaths() {
        List<String> apps = new ArrayList<>();
        File apkFolder = new File(res.getString(APK_ARG.toString()));
        if (!apkFolder.isDirectory()) {
            return Collections.singletonList("");
        } else {
            searchApkInFolder(apkFolder, apps);
        }
        return apps;
    }

    /**
     * 递归在目录下查找APK文件
     *
     * @param folder   目标目录
     * @param appsPath 存放搜索到APK文件路径的列表
     */
    private void searchApkInFolder(File folder, List<String> appsPath) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    searchApkInFolder(file, appsPath);
                } else if (file.getName().endsWith(".apk")) {
                    appsPath.add(file.getPath());
                }
            }
        }
    }

    /**
     * 返回列表下APK文件的名称
     *
     * @param appsPaths 通过{@link #getAppsPaths()}获取的APK路径的列表
     */
    public List<String> getAppsFileNames(List<String> appsPaths) {
        return appsPaths.stream()
                .map(item -> removeAPKExtension(new File(item).getName()))
                .collect(Collectors.toList());
    }

    /**
     * 移除APK文件名称的.apk后缀
     *
     * @param original 原始路径字符串
     * @return 去除后缀的APK名称
     */
    private String removeAPKExtension(String original) {
        return original.substring(0, original.length() - 4);
    }

    /**
     * 验证传递给Paprika分析的File是否为文件夹
     *
     * @return True, 目录, 否则False
     */
    public boolean isFolderMode() {
        File apkFolder = new File(res.getString(APK_ARG.toString()));
        return apkFolder.isDirectory();
    }
}
