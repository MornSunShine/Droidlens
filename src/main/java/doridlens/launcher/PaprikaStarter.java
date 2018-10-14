package doridlens.launcher;

import doridlens.launcher.arg.PaprikaArgParser;
import doridlens.query.neo4j.QueryEngine;
import doridlens.query.neo4j.queries.QueryPropertiesException;
import doridlens.query.neo4j.queries.QueryPropertiesReader;

import java.io.IOException;
import java.io.PrintStream;

import static doridlens.launcher.arg.Argument.THRESHOLDS_ARG;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 21:16
 * Description: Paprika各模式启动器抽象类
 */
public abstract class PaprikaStarter {

    protected PaprikaArgParser argParser;
    protected PrintStream out;

    /**
     * 构造函数
     *
     * @param argParser 提供Paprika解析的参数解析器
     * @param out       执行反馈输出口
     */
    public PaprikaStarter(PaprikaArgParser argParser, PrintStream out) {
        this.argParser = argParser;
        this.out = out;
    }

    /**
     * 主要功能函数,各模式分别继承后针对重写
     */
    public abstract void start();

    protected QueryEngine createQueryEngine() throws IOException, QueryPropertiesException {
        QueryPropertiesReader reader = new QueryPropertiesReader();
        reader.loadProperties(argParser.getArg(THRESHOLDS_ARG));
        return new QueryEngine(argParser, reader);
    }

}

