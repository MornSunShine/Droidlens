package doridlens.launcher;

import doridlens.launcher.arg.PaprikaArgException;
import doridlens.launcher.arg.PaprikaArgParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;

import java.io.PrintStream;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 21:15
 * Description: Paprika会话启动器
 */
public class PaprikaLauncher {

    private PaprikaArgParser argParser;
    private PrintStream out;

    /**
     * 构造函数
     *
     * @param args 命令行参数
     * @param out  反馈输出口
     * @throws PaprikaArgException 非法参数报错
     */
    public PaprikaLauncher(String[] args, PrintStream out) throws PaprikaArgException {
        this.out = out;
        this.argParser = new PaprikaArgParser();
        try {
            argParser.parseArgs(args);
        } catch (ArgumentParserException e) {
            argParser.handleError(e);
        }
    }

    /**
     * 检测启动
     */
    public void startPaprika() {
        PaprikaStarter starter = argParser.getSelectedStarter(out);
        if (starter != null) {
            starter.start();
        }
    }

    /**
     * 获取工具参数
     * @return 命令行参数解析实例
     */
    public PaprikaArgParser getArgParser() {
        return argParser;
    }

}
