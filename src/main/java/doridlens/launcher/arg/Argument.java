package doridlens.launcher.arg;

import doridlens.analyse.ApkPropertiesParser;
import doridlens.analyse.entities.PaprikaAppBuilder;
import doridlens.launcher.PaprikaMode;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.Subparser;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static doridlens.analyse.entities.PaprikaApp.NO_SDK;
import static doridlens.launcher.PaprikaMode.*;

/**
 * Author: MaoMorn
 * Date: 2018/9/20
 * Time: 21:17
 * Description: Paprika中的操作参数,Enum枚举类型
 */
public enum Argument {

    APK_ARG("apk", ANALYSE_MODE) {
        @Override
        public void setup(Subparser subParser) {
            subParser.addArgument(toString()).help("Path of the APK to analyze");
        }
    },

    ANDROID_JARS_ARG("androidJars", ANALYSE_MODE) {
        @Override
        public void setup(Subparser subParser) {
            subParser.addArgument("-a", prefixArg(toString())).required(true)
                    .help("Path to android platforms jars");
        }
    },

    DATABASE_ARG("database", ALL) {
        @Override
        public void setup(Subparser subParser) {
            subParser.addArgument("-db", prefixArg(toString())).required(true)
                    .help("Path to neo4J Database folder (will be created if none is found on the given path)");
        }
    },

    UNSAFE_ARG("unsafe", ANALYSE_MODE) {
        @Override
        public void setup(Subparser subParser) {
            subParser.addArgument("-u", prefixArg(toString()))
                    .action(Arguments.storeTrue())
                    .help("Unsafe mode (no args checking)");
        }
    },

    ONLY_MAIN_PACKAGE_ARG("onlyMainPackage", ANALYSE_MODE) {
        @Override
        public void setup(Subparser subParser) {
            subParser.addArgument("-omp", prefixArg(toString()))
                    .action(Arguments.storeTrue())
                    .help("Analyze only the main package of the application");
        }
    },

    FORCE_ANALYSIS_ARG("forceAnalysis", ANALYSE_MODE) {
        @Override
        public void setup(Subparser subParser) {
            subParser.addArgument("-f", prefixArg(toString()))
                    .action(Arguments.storeTrue())
                    .help("Attempt to analyze even if Soot may not handle this Android API version");
        }
    },

    // --------------------------------------------------------------
    // ------------------- APP PROPS ARGS ---------------------------
    // --------------------------------------------------------------

    NAME_ARG("name", ANALYSE_MODE) {
        @Override
        public void setup(Subparser subParser) {
            subParser.addArgument("-n", prefixArg(toString()))
                    .setDefault("")
                    .help("Name of the application (or the apk filename will be used as name)");
        }

        @Override
        public void insertAppProperty(ApkPropertiesParser parser, PaprikaAppBuilder builder) {
            readArgProperty(parser, builder, builder::name);
        }
    },

    PACKAGE_ARG("package", ANALYSE_MODE) {
        @Override
        public void setup(Subparser subParser) {
            subParser.addArgument("-p", prefixArg(toString()))
                    .setDefault("")
                    .help("Application main package");
        }

        @Override
        public void insertAppProperty(ApkPropertiesParser parser, PaprikaAppBuilder builder) {
            readArgProperty(parser, builder, builder::pack);
        }
    },

    KEY_ARG("key", ANALYSE_MODE) {
        @Override
        public void setup(Subparser subParser) {
            subParser.addArgument("-k", prefixArg(toString()))
                    .help("sha256 of the apk used as identifier");
        }

        @Override
        public void insertAppProperty(ApkPropertiesParser parser, PaprikaAppBuilder builder) {
            readArgProperty(parser, builder, builder::key);
        }
    },

    DEVELOPER_ARG("developer", ANALYSE_MODE) {
        @Override
        public void setup(Subparser subParser) {
            subParser.addArgument("-dev", prefixArg(toString())).setDefault("default-developer")
                    .help("Application developer");
        }

        @Override
        public void insertAppProperty(ApkPropertiesParser parser, PaprikaAppBuilder builder) {
            readArgProperty(parser, builder, builder::developer);
        }
    },

    CATEGORY_ARG("category", ANALYSE_MODE) {
        @Override
        public void setup(Subparser subParser) {
            subParser.addArgument("-cat", prefixArg(toString())).setDefault("default-category")
                    .help("Application category");
        }

        @Override
        public void insertAppProperty(ApkPropertiesParser parser, PaprikaAppBuilder builder) {
            readArgProperty(parser, builder, builder::category);
        }
    },

    NB_DOWNLOAD_ARG("nbDownload", ANALYSE_MODE) {
        @Override
        public void setup(Subparser subParser) {
            subParser.addArgument("-nd", prefixArg(toString())).setDefault(0)
                    .type(Integer.class)
                    .help("Numbers of downloads for the app");
        }

        @Override
        public void insertAppProperty(ApkPropertiesParser parser, PaprikaAppBuilder builder) {
            readArgProperty(parser, builder, nb -> builder.nbDownload(Integer.parseInt(nb)));
        }
    },

    DATE_ARG("date", ANALYSE_MODE) {
        @Override
        public void setup(Subparser subParser) {
            subParser.addArgument("-d", prefixArg(toString())).setDefault("0000-01-01 00:00:00.000000")
                    .help("Date of download");
        }

        @Override
        public void insertAppProperty(ApkPropertiesParser parser, PaprikaAppBuilder builder) {
            readArgProperty(parser, builder, builder::date);
        }
    },

    RATING_ARG("rating", ANALYSE_MODE) {
        @Override
        public void setup(Subparser subParser) {
            subParser.addArgument("-r", prefixArg(toString())).type(Double.class)
                    .setDefault(1.0)
                    .help("application rating");
        }

        @Override
        public void insertAppProperty(ApkPropertiesParser parser, PaprikaAppBuilder builder) {
            readArgProperty(parser, builder, rating -> builder.rating(Double.parseDouble(rating)));
        }
    },

    SIZE_ARG("size", ANALYSE_MODE) {
        @Override
        public void setup(Subparser subParser) {
            subParser.addArgument("-s", prefixArg(toString())).type(Integer.class)
                    .setDefault(1)
                    .help("Size of the application");
        }

        @Override
        public void insertAppProperty(ApkPropertiesParser parser, PaprikaAppBuilder builder) {
            readArgProperty(parser, builder, size -> builder.size(Integer.parseInt(size)));
        }
    },

    VERSION_CODE_ARG("versionCode", ANALYSE_MODE) {
        @Override
        public void setup(Subparser subParser) {
            subParser.addArgument("-vc", prefixArg(toString()))
                    .setDefault("")
                    .help("Version Code of the application (extracted from manifest)");
        }

        @Override
        public void insertAppProperty(ApkPropertiesParser parser, PaprikaAppBuilder builder) {
            readArgProperty(parser, builder, builder::versionCode);
        }
    },

    VERSION_NAME_ARG("versionName", ANALYSE_MODE) {
        @Override
        public void setup(Subparser subParser) {
            subParser.addArgument("-vn", prefixArg(toString())).setDefault("")
                    .help("Version Name of the application (extracted from manifest)");
        }

        @Override
        public void insertAppProperty(ApkPropertiesParser parser, PaprikaAppBuilder builder) {
            readArgProperty(parser, builder, builder::versionName);
        }
    },

    TARGET_SDK_VERSION_ARG("targetSdkVersion", ANALYSE_MODE) {
        @Override
        public void setup(Subparser subParser) {
            subParser.addArgument("-tsdk", prefixArg(toString()))
                    .setDefault(NO_SDK)
                    .type(Integer.class)
                    .help("Target SDK Version (extracted from manifest)");
        }

        @Override
        public void insertAppProperty(ApkPropertiesParser parser, PaprikaAppBuilder builder) {
            readArgProperty(parser, builder, target -> builder.targetSdkVersion(Integer.parseInt(target)));
        }
    },

    SDK_VERSION_ARG("sdkVersion", ANALYSE_MODE) {
        @Override
        public void setup(Subparser subParser) {
            subParser.addArgument("-sdk", prefixArg(toString()))
                    .setDefault(NO_SDK)
                    .type(Integer.class)
                    .help("sdk version (extracted from manifest)");
        }

        @Override
        public void insertAppProperty(ApkPropertiesParser parser, PaprikaAppBuilder builder) {
            readArgProperty(parser, builder, sdk -> builder.sdkVersion(Integer.parseInt(sdk)));
        }
    },

    PRICE_ARG("price", ANALYSE_MODE) {
        @Override
        public void setup(Subparser subParser) {
            subParser.addArgument("-pr", prefixArg(toString())).setDefault("Free")
                    .help("Price of the application");
        }

        @Override
        public void insertAppProperty(ApkPropertiesParser parser, PaprikaAppBuilder builder) {
            readArgProperty(parser, builder, builder::price);
        }
    },

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    // --------------------------------------------------------------

    REQUEST_ARG("request", QUERY_MODE) {
        @Override
        public void setup(Subparser subParser) {
            subParser.addArgument("-r", prefixArg(toString())).required(true)
                    .help("Request to execute");
        }
    },

    CSV_ARG("csv", QUERY_MODE) {
        @Override
        public void setup(Subparser subParser) {
            subParser.addArgument("-c", prefixArg(toString())).help("Path to register csv files").setDefault("");
        }
    },

    DELETE_KEY("deleteKey", DELETE_MODE) {
        @Override
        public void setup(Subparser subParser) {
            subParser.addArgument("-dk", prefixArg(toString())).required(true)
                    .help("App key of the app to delete");
        }
    },

    DETAILS_ARG("details", QUERY_MODE) {
        @Override
        public void setup(Subparser subParser) {
            subParser.addArgument("-d", prefixArg(toString()))
                    .action(Arguments.storeTrue())
                    .help("Show the concerned entity in the results");
        }
    },

    THRESHOLDS_ARG("thresholds", QUERY_MODE) {
        @Override
        public void setup(Subparser subParser) {
            subParser.addArgument("-thr", prefixArg(toString()))
                    .setDefault((String) null)
                    .help("Path to .properties file containing android thresholds");
        }
    },

    NO_NAMES_ARG("noNames", QUERY_MODE) {
        @Override
        public void setup(Subparser subParser) {
            subParser.addArgument("-nn", prefixArg(toString())).type(Boolean.class)
                    .setDefault(false).action(Arguments.storeTrue())
                    .help("Disable app names in output");
        }
    };

    public static final Argument[] ANALYSE_PROPS_ARGS = {
            PRICE_ARG, PACKAGE_ARG, KEY_ARG, DEVELOPER_ARG, CATEGORY_ARG,
            NB_DOWNLOAD_ARG, DATE_ARG, RATING_ARG, SIZE_ARG, VERSION_CODE_ARG,
            VERSION_NAME_ARG, TARGET_SDK_VERSION_ARG, SDK_VERSION_ARG,
    };

    private String name;
    private PaprikaMode category;

    /**
     * 构造函数
     *
     * @param name     不带"--"前缀的长参数名
     * @param category 参数归属的Paprika执行模式名
     */
    Argument(String name, PaprikaMode category) {
        this.name = name;
        this.category = category;
    }

    /**
     * 返回属于指定模式的所有参数列表
     */
    public static List<Argument> getAllArguments(PaprikaMode category) {
        return Arrays.stream(values())
                .filter(arg -> arg.category.equals(category) || arg.category.equals(ALL))
                .collect(Collectors.toList());
    }

    /**
     * 参数信息初始化
     *
     * @param subParser 子解析器实例
     */
    public abstract void setup(Subparser subParser);

    /**
     * 从JSON文件中解析匹配该参数的属性信息(比如rating, size,等.),如果有，将其添加到模型构建器
     *
     * @param parser  APK属性解析器
     * @param builder 模型构建器实例
     */
    public void insertAppProperty(ApkPropertiesParser parser, PaprikaAppBuilder builder) {
        throw new UnsupportedOperationException("This argument cannot be passed as an app property.");
    }

    /**
     * 读取与参数名称匹配的属性信息
     * 如果找到属性信息,使用所给函数进行处理
     *
     * @param parser         JSON属性解析器
     * @param builder        模型构建器实例
     * @param applyIfPresent 属性值被发现时的处理函数
     */
    protected void readArgProperty(ApkPropertiesParser parser, PaprikaAppBuilder builder,
                                   Function<String, PaprikaAppBuilder> applyIfPresent) {
        String elem = parser.getAppProperty(builder.getName(), toString());
        if (elem != null) {
            applyIfPresent.apply(elem);
        }
    }

    protected String prefixArg(String arg) {
        return "--" + arg;
    }

    @Override
    public String toString() {
        return name;
    }

}