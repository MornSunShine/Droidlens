package doridlens.query.commands;

import doridlens.analyse.metrics.classes.stat.paprika.ClassComplexity;
import doridlens.analyse.metrics.classes.stat.paprika.LackOfCohesionInMethods;
import doridlens.analyse.metrics.common.NumberOfMethods;
import doridlens.analyse.metrics.methods.stat.CyclomaticComplexity;
import doridlens.query.neo4j.QueryEngine;
import doridlens.query.neo4j.queries.PaprikaQuery;
import doridlens.query.neo4j.queries.QueryPropertiesReader;
import doridlens.query.neo4j.queries.antipatterns.LongParameterList;
import doridlens.query.neo4j.queries.antipatterns.adoctor.DurableWakelock;
import doridlens.query.neo4j.queries.antipatterns.adoctor.PublicData;
import doridlens.query.neo4j.queries.antipatterns.adoctor.RigidAlarmManager;
import doridlens.query.neo4j.queries.antipatterns.fuzzy.*;
import doridlens.query.neo4j.queries.antipatterns.manifest.DebuggableRelease;
import doridlens.query.neo4j.queries.antipatterns.manifest.SetConfigChanges;
import doridlens.query.neo4j.queries.antipatterns.memory.*;
import doridlens.query.neo4j.queries.antipatterns.optimization.*;
import doridlens.query.neo4j.queries.antipatterns.ui.*;
import doridlens.query.neo4j.queries.stats.*;

import java.util.Arrays;
import java.util.Collections;

import static doridlens.analyse.neo4j.ModelToGraph.CLASS_TYPE;
import static doridlens.analyse.neo4j.ModelToGraph.METHOD_TYPE;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 21:21
 * Description: Smell请求查询参数,Enum枚举类型
 */
public enum PaprikaRequest {

    HMU(HashMapUsage.KEY) {
        @Override
        public PaprikaCommand getCommand(QueryEngine engine) {
            return getSimpleCommand(engine, new HashMapUsage());
        }
    },

    IGS(InternalGetterSetter.KEY) {
        @Override
        public PaprikaCommand getCommand(QueryEngine engine) {
            return getSimpleCommand(engine, new InternalGetterSetter());
        }
    },

    IOD(InitOnDraw.KEY) {
        @Override
        public PaprikaCommand getCommand(QueryEngine engine) {
            return getSimpleCommand(engine, new InitOnDraw());
        }
    },

    IWR(InvalidateWithoutRect.KEY) {
        @Override
        public PaprikaCommand getCommand(QueryEngine engine) {
            return getSimpleCommand(engine, new InvalidateWithoutRect());
        }
    },

    LIC(LeakingInnerClass.KEY) {
        @Override
        public PaprikaCommand getCommand(QueryEngine engine) {
            return getSimpleCommand(engine, new LeakingInnerClass());
        }
    },

    MIM(MemberIgnoringMethod.KEY) {
        @Override
        public PaprikaCommand getCommand(QueryEngine engine) {
            return getSimpleCommand(engine, new MemberIgnoringMethod());
        }
    },

    NLMR(NoLowMemoryResolver.KEY) {
        @Override
        public PaprikaCommand getCommand(QueryEngine engine) {
            return getSimpleCommand(engine, new NoLowMemoryResolver());
        }
    },

    UIO(Overdraw.KEY) {
        @Override
        public PaprikaCommand getCommand(QueryEngine engine) {
            return getSimpleCommand(engine, new Overdraw());
        }
    },

    UHA_CMD(UHA.KEY) {
        @Override
        public PaprikaCommand getCommand(QueryEngine engine) {
            return getSimpleCommand(engine, new UHA());
        }
    },

    UCS(UnsuitedLRUCache.KEY) {
        @Override
        public PaprikaCommand getCommand(QueryEngine engine) {
            return getSimpleCommand(engine, new UnsuitedLRUCache());
        }
    },

    BLOB_CMD(BLOB.KEY) {
        @Override
        public PaprikaCommand getCommand(QueryEngine engine) {
            return getFuzzyCommand(engine, new BLOB(engine.getPropsReader()));
        }
    },

    CC_CMD(ComplexClass.KEY) {
        @Override
        public PaprikaCommand getCommand(QueryEngine engine) {
            return getFuzzyCommand(engine, new ComplexClass(engine.getPropsReader()));
        }
    },

    HAS(HeavyAsyncTaskSteps.KEY) {
        @Override
        public PaprikaCommand getCommand(QueryEngine engine) {
            return getFuzzyCommand(engine, new HeavyAsyncTaskSteps(engine.getPropsReader()));
        }
    },

    HBR(HeavyBroadcastReceiver.KEY) {
        @Override
        public PaprikaCommand getCommand(QueryEngine engine) {
            return getFuzzyCommand(engine, new HeavyBroadcastReceiver(engine.getPropsReader()));
        }
    },

    HSS(HeavyServiceStart.KEY) {
        @Override
        public PaprikaCommand getCommand(QueryEngine engine) {
            return getFuzzyCommand(engine, new HeavyServiceStart(engine.getPropsReader()));
        }
    },

    LM(LongMethod.KEY) {
        @Override
        public PaprikaCommand getCommand(QueryEngine engine) {
            return getFuzzyCommand(engine, new LongMethod(engine.getPropsReader()));
        }
    },

    SAK(SwissArmyKnife.KEY) {
        @Override
        public PaprikaCommand getCommand(QueryEngine engine) {
            return getFuzzyCommand(engine, new SwissArmyKnife(engine.getPropsReader()));
        }
    },

    DR(DebuggableRelease.KEY) {
        @Override
        public PaprikaCommand getCommand(QueryEngine engine) {
            return getSimpleCommand(engine, new DebuggableRelease());
        }
    },

    SCC(SetConfigChanges.KEY) {
        @Override
        public PaprikaCommand getCommand(QueryEngine engine) {
            return getSimpleCommand(engine, new SetConfigChanges());
        }
    },

    DW(DurableWakelock.KEY) {
        @Override
        public PaprikaCommand getCommand(QueryEngine engine) {
            return getSimpleCommand(engine, new DurableWakelock());
        }
    },

    PD(PublicData.KEY) {
        @Override
        public PaprikaCommand getCommand(QueryEngine engine) {
            return getSimpleCommand(engine, new PublicData());
        }
    },

    RAM(RigidAlarmManager.KEY) {
        @Override
        public PaprikaCommand getCommand(QueryEngine engine) {
            return getSimpleCommand(engine, new RigidAlarmManager());
        }
    },

    LPL(LongParameterList.KEY) {
        @Override
        public PaprikaCommand getCommand(QueryEngine engine) {
            return getSimpleCommand(engine, new LongParameterList());
        }
    },


    // -----------------------------------------------------------------------------------

    ALL_HEAVY("ALLHEAVY") {
        @Override
        public PaprikaCommand getCommand(QueryEngine engine) {
            return new QueriesCommand(engine, Arrays.asList(
                    new HeavyAsyncTaskSteps(engine.getPropsReader()),
                    new HeavyBroadcastReceiver(engine.getPropsReader()),
                    new HeavyServiceStart(engine.getPropsReader())
            ));
        }
    },

    ANALYZED(AnalyzedAppQuery.COMMAND_KEY) {
        @Override
        public PaprikaCommand getCommand(QueryEngine engine) {
            return getSimpleCommand(engine, new AnalyzedAppQuery());
        }
    },


    STATS(StatsCommand.KEY) {
        @Override
        public PaprikaCommand getCommand(QueryEngine engine) {
            return new StatsCommand(engine);
        }
    },

    ALL_LCOM(PropertyQuery.ALL_LCOM) {
        @Override
        public PaprikaCommand getCommand(QueryEngine engine) {
            return getSimpleCommand(engine, new PropertyQuery("ALL_LCOM", CLASS_TYPE, LackOfCohesionInMethods.NAME));
        }
    },

    ALL_CYCLO(PropertyQuery.ALL_CC) {
        @Override
        public PaprikaCommand getCommand(QueryEngine engine) {
            return getSimpleCommand(engine, new PropertyQuery("ALL_CLASS_COMPLEXITY", CLASS_TYPE, ClassComplexity.NAME));
        }
    },

    ALL_CC(PropertyQuery.ALL_CYCLOMATIC) {
        @Override
        public PaprikaCommand getCommand(QueryEngine engine) {
            return getSimpleCommand(engine, new PropertyQuery("ALL_CYCLOMATIC_COMPLEXITY", METHOD_TYPE, CyclomaticComplexity.NAME));
        }
    },

    ALL_NUM_METHOD(PropertyQuery.ALL_METHODS) {
        @Override
        public PaprikaCommand getCommand(QueryEngine engine) {
            return getSimpleCommand(engine, new PropertyQuery("ALL_NUMBER_OF_METHODS", CLASS_TYPE, NumberOfMethods.NAME));
        }
    },

    COUNT_VARS(CountVariablesQuery.COMMAND_KEY) {
        @Override
        public PaprikaCommand getCommand(QueryEngine engine) {
            return getSimpleCommand(engine, new CountVariablesQuery());
        }
    },

    COUNT_INNER(CountInnerQuery.COMMAND_KEY) {
        @Override
        public PaprikaCommand getCommand(QueryEngine engine) {
            return getSimpleCommand(engine, new CountInnerQuery());
        }
    },

    COUNT_ASYNC(CountAsyncQuery.COMMAND_KEY) {
        @Override
        public PaprikaCommand getCommand(QueryEngine engine) {
            return getSimpleCommand(engine, new CountAsyncQuery());
        }
    },

    COUNT_VIEWS(CountViewsQuery.COMMAND_KEY) {
        @Override
        public PaprikaCommand getCommand(QueryEngine engine) {
            return getSimpleCommand(engine, new CountViewsQuery());
        }
    },

    NON_FUZZY("NONFUZZY") {
        @Override
        public PaprikaCommand getCommand(QueryEngine engine) {
            return new QueriesCommand(engine, Arrays.asList(
                    new InternalGetterSetter(), new MemberIgnoringMethod(), new LeakingInnerClass(),
                    new NoLowMemoryResolver(), new Overdraw(), new UnsuitedLRUCache(),
                    new InitOnDraw(), new UHA(), new HashMapUsage(),new LongParameterList(),
                    new InvalidateWithoutRect(), new DebuggableRelease(), new SetConfigChanges(),
                    new DurableWakelock(), new PublicData(), new RigidAlarmManager()
            ));
        }
    },

    FUZZY("FUZZY") {
        @Override
        public PaprikaCommand getCommand(QueryEngine engine) {
            QueryPropertiesReader reader = engine.getPropsReader();
            return new FuzzyCommand(engine, Arrays.asList(
                    new ComplexClass(reader), new LongMethod(reader), new SwissArmyKnife(reader),
                    new BLOB(reader), new HeavyServiceStart(reader),
                    new HeavyBroadcastReceiver(reader),
                    new HeavyAsyncTaskSteps(reader)
            ));
        }
    },

    ALLAP(ALLAPCommand.KEY) {
        @Override
        public PaprikaCommand getCommand(QueryEngine engine) {
            return new ALLAPCommand(engine, FUZZY, NON_FUZZY);
        }
    },

    FORCE_NO_FUZZY("FORCENOFUZZY") {
        @Override
        public PaprikaCommand getCommand(QueryEngine engine) {
            QueryPropertiesReader reader = engine.getPropsReader();
            return new QueriesCommand(engine, Arrays.asList(
                    new ComplexClass(reader), new LongMethod(reader), new SwissArmyKnife(reader), new BLOB(reader),
                    new HeavyServiceStart(reader), new HeavyBroadcastReceiver(reader),
                    new HeavyAsyncTaskSteps(reader)
            ));
        }
    };

    private String key;

    /**
     * 构造函数
     *
     * @param key 被用在Query模式下的指令字符串
     */
    PaprikaRequest(String key) {
        this.key = key;
    }

    /**
     * 获取请求指令实例
     *
     * @param command 目标指令实例的名称
     */
    public static PaprikaRequest getRequest(String command) {
        for (PaprikaRequest request : PaprikaRequest.values()) {
            if (request.key.equals(command)) {
                return request;
            }
        }
        return null;
    }

    /**
     * 创建一次PaprikaQuery命令,负责Android相关的Smell查询
     *
     * @param engine 查询引擎实例
     * @param query  运行的查询
     * @return Android Smell查询实例
     */
    public static PaprikaCommand getSimpleCommand(QueryEngine engine, PaprikaQuery query) {
        return new QueriesCommand(engine, Collections.singletonList(query));
    }

    /**
     * 参看{@link #getSimpleCommand(QueryEngine, PaprikaQuery)}
     * 创建一个FuzzyQuery实例,负责Common Smell查询
     */
    public static PaprikaCommand getFuzzyCommand(QueryEngine engine, FuzzyQuery query) {
        return new FuzzyCommand(engine, Collections.singletonList(query));
    }

    /**
     * 创建匹配请求的查询实例
     *
     * @param engine 运行查询的引擎实例
     * @return 查询Command实例
     */
    public abstract PaprikaCommand getCommand(QueryEngine engine);
}
