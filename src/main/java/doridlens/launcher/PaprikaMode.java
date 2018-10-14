package doridlens.launcher;

import doridlens.DeleteModeStarter;
import doridlens.analyse.AnalyseModeStarter;
import doridlens.launcher.arg.Argument;
import doridlens.launcher.arg.PaprikaArgParser;
import doridlens.query.QueryModeStarter;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;

import java.io.PrintStream;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 21:15
 * Description: Paprika下的支持的模式,Enum
 */
public enum PaprikaMode {

    ANALYSE_MODE("analyse", "Analyse an app") {
        @Override
        public PaprikaStarter getStarter(PaprikaArgParser parser, PrintStream out) {
            return new AnalyseModeStarter(parser, out);
        }
    },

    QUERY_MODE("query", "Query the database") {
        @Override
        public PaprikaStarter getStarter(PaprikaArgParser parser, PrintStream out) {
            return new QueryModeStarter(parser, out);
        }
    },

    DELETE_MODE("delete", "Delete apps from the database") {
        @Override
        public PaprikaStarter getStarter(PaprikaArgParser parser, PrintStream out) {
            return new DeleteModeStarter(parser, out);
        }
    },

    /**
     * 该分类下包括通用的参数,避免各执行模式下,参数的重复,例如 -db
     */
    ALL("all", "all-help") {
        @Override
        public PaprikaStarter getStarter(PaprikaArgParser parser, PrintStream out) {
            throw new UnsupportedOperationException(this.name + " is not a valid Paprika argument");
        }

        @Override
        public void setupAllArgs(Subparsers subParsers) {
            // Do nothing
        }

    };

    protected String name;
    private String help;
    private Subparser subParser;

    /**
     * 构造函数
     *
     * @param name 模式名称
     * @param help 模式的描述信息
     */
    PaprikaMode(String name, String help) {
        this.name = name;
        this.help = help;
    }

    /**
     * 返回该模式对应的执行程序实例
     *
     * @param parser Paprika参数解析器
     * @param out    反馈信息输出端口
     */
    public abstract PaprikaStarter getStarter(PaprikaArgParser parser, PrintStream out);

    /**
     * 初始化该模式下的参数列表
     *
     * @param subParsers 子解析器实例
     */
    public void setupAllArgs(Subparsers subParsers) {
        subParser = subParsers.addParser(name).help(help);
        for (Argument arg : Argument.getAllArguments(this)) {
            arg.setup(subParser);
        }
    }

    /**
     * 根据模式名称获取对应的模式实例
     *
     * @param modeName 模式名称
     */
    public static PaprikaMode getMode(String modeName) {
        for (PaprikaMode mode : values()) {
            if (mode.name.equals(modeName)) {
                return mode;
            }
        }
        return null;
    }

    public Subparser getSubParser() {
        return subParser;
    }

    @Override
    public String toString() {
        return name;
    }

}
