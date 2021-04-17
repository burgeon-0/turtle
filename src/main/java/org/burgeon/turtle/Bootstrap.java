package org.burgeon.turtle;

import org.apache.commons.cli.*;
import org.burgeon.turtle.bootstrap.BootstrapException;
import org.burgeon.turtle.bootstrap.notifier.BootstrapApiBlueprintNotifier;
import org.burgeon.turtle.bootstrap.notifier.BootstrapJmeterNotifier;
import org.burgeon.turtle.bootstrap.notifier.BootstrapPostmanNotifier;
import org.burgeon.turtle.core.common.ConfigInitializer;
import org.burgeon.turtle.core.common.Constants;
import org.burgeon.turtle.core.common.VersionHelper;
import org.burgeon.turtle.core.event.EventTarget;
import org.burgeon.turtle.core.process.DefaultProcessor;
import org.burgeon.turtle.core.process.Notifier;
import org.burgeon.turtle.core.process.Processor;
import org.burgeon.turtle.export.DefaultExporterConfig;
import org.burgeon.turtle.core.utils.EnvUtils;

import java.util.Properties;

/**
 * 启动程序
 *
 * @author luxiaocong
 * @createdOn 2021/3/4
 */
public class Bootstrap {

    private static final String OPTION_UD = "D";
    private static final String OPTION_D = "d";
    private static final String OPTION_H = "h";
    private static final String OPTION_V = "v";
    private static final String OPTION_I = "i";
    private static final String OPTION_O = "o";
    private static final String OPTION_C = "c";
    private static final String OPTION_E = "e";

    /**
     * -e 指定导出内容：blueprint、postman、jmeter
     * blueprint：导出API blueprint文档
     * postman：导出Postman测试用例
     * jmeter：导出Apache JMeter测试用例
     *
     * @param args
     */
    public static void main(String[] args) {
        // create the command line parser
        CommandLineParser parser = new DefaultParser();

        // create the Options
        Options options = buildOptions();
        try {
            // parse the command line arguments
            CommandLine line = parser.parse(options, args);
            // 初始化需要优先初始化的配置：如项目根路径、Debug配置等
            initPriorityConfig(line);

            if (line.getOptions().length == 0 || line.hasOption(OPTION_H)) {
                // display help information
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp(Constants.PROJECT_NAME, options);
            } else if (line.hasOption(OPTION_V)) {
                // display version information
                info("Turtle version " + VersionHelper.VERSION);
            } else {
                execute(line);
            }
        } catch (Exception e) {
            if (EnvUtils.getBooleanProperty(Constants.DEBUG, false)) {
                e.printStackTrace();
            } else {
                if (e.getMessage() != null) {
                    error(e.getMessage());
                } else {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 构建options
     *
     * @return
     */
    private static Options buildOptions() {
        Options options = new Options();
        Option property = Option.builder(OPTION_UD)
                .argName("property=value")
                .valueSeparator()
                .numberOfArgs(2)
                .desc("Use value for given property")
                .build();
        options.addOption(property);
        options.addOption(OPTION_D, "debug", false, "Turn on debug mode");
        options.addOption(OPTION_H, "help", false, "Display help information");
        options.addOption(OPTION_V, "version", false, "Display version information");
        options.addOption(OPTION_I, "input", true,
                "Specify source directory, it always is your project's root folder");
        options.addOption(OPTION_O, "output", true,
                "Specify target directory, where you want to store the artifacts");
        options.addOption(OPTION_C, "classpath", true,
                "Specify classpath, which can be directories or jar files, multiple paths can be split by ';'");
        options.addOption(OPTION_E, "export", true, "blueprint | postman | jmeter");
        return options;
    }

    /**
     * 初始化需要优先初始化的配置
     *
     * @param line
     */
    private static void initPriorityConfig(CommandLine line) {
        // set home path
        if (System.getenv(Constants.TURTLE_HOME) != null) {
            System.setProperty(Constants.TURTLE_HOME, System.getenv(Constants.TURTLE_HOME));
        } else {
            String homePath = System.getProperty("user.dir");
            String binPath = Constants.BIN_DIR;
            if (homePath.endsWith(binPath)) {
                homePath = homePath.substring(0, homePath.length() - binPath.length());
            }
            System.setProperty(Constants.TURTLE_HOME, homePath);
        }

        // set config path
        boolean setConfPath = false;
        if (line.hasOption(OPTION_UD)) {
            Properties properties = line.getOptionProperties(OPTION_UD);
            if (properties != null) {
                if (properties.getProperty(Constants.CONF_PATH) != null) {
                    System.setProperty(Constants.CONF_PATH, properties.getProperty(Constants.CONF_PATH));
                    setConfPath = true;
                }
            }
        }
        if (!setConfPath) {
            System.setProperty(Constants.CONF_PATH, Constants.CONF_DIR);
        }

        // set debug info
        if (line.hasOption(OPTION_D)) {
            System.setProperty(Constants.DEBUG, "true");
            System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "debug");
        }
    }

    /**
     * 处理
     *
     * @param line
     * @throws BootstrapException
     */
    private static void execute(CommandLine line) throws BootstrapException {
        // 初始化配置文件中的配置
        ConfigInitializer.init();

        // 初始化运行时指定的配置
        // 配置文件中的配置，会被运行时指定的配置覆盖
        initAnalysisConfig(line);
        initDirectoryConfig(line);

        // 执行核心操作
        if (line.hasOption(OPTION_E)) {
            String export = line.getOptionValue(OPTION_E);
            doExecute(export);
        } else {
            info("Argument: -e is required");
        }
    }

    /**
     * 初始化analysis配置
     *
     * @param line
     */
    private static void initAnalysisConfig(CommandLine line) {
        if (line.hasOption(OPTION_UD)) {
            Properties properties = line.getOptionProperties(OPTION_UD);
            if (properties != null) {
                if (properties.getProperty(Constants.ANALYSIS_ORDER) != null) {
                    System.setProperty(Constants.ANALYSIS_ORDER, properties.getProperty(Constants.ANALYSIS_ORDER));
                }
                if (properties.getProperty(Constants.ANALYSIS_MODE) != null) {
                    System.setProperty(Constants.ANALYSIS_MODE, properties.getProperty(Constants.ANALYSIS_MODE));
                }
            }
        }
    }

    /**
     * 初始化directory配置
     *
     * @param line
     */
    private static void initDirectoryConfig(CommandLine line) {
        String currentPath = System.getProperty("user.dir");
        if (line.hasOption(OPTION_I)) {
            String sourcePath = line.getOptionValue(OPTION_I);
            System.setProperty(Constants.SOURCE_PATH, sourcePath);
        } else {
            System.setProperty(Constants.SOURCE_PATH, currentPath);
        }
        if (line.hasOption(OPTION_O)) {
            String targetPath = line.getOptionValue(OPTION_O);
            System.setProperty(Constants.TARGET_PATH, targetPath);
        } else {
            System.setProperty(Constants.TARGET_PATH, currentPath + Constants.OUT_DIR);
        }
        if (line.hasOption(OPTION_C)) {
            String classpath = line.getOptionValue(OPTION_C);
            System.setProperty(Constants.CLASSPATH, classpath);
        }
    }

    /**
     * 执行处理
     *
     * @param export
     * @throws BootstrapException
     */
    private static void doExecute(String export) throws BootstrapException {
        if (!EventTarget.contains(export)) {
            throw new BootstrapException("Unknown export type: " + export);
        }

        DefaultExporterConfig exporterConfig = new DefaultExporterConfig();
        exporterConfig.init();

        // 进行处理
        Processor processor = new DefaultProcessor();
        Notifier notifier = null;
        if (EventTarget.fromName(export) == EventTarget.BLUEPRINT) {
            notifier = new BootstrapApiBlueprintNotifier();
        } else if (EventTarget.fromName(export) == EventTarget.POSTMAN) {
            notifier = new BootstrapPostmanNotifier();
        } else if (EventTarget.fromName(export) == EventTarget.JMETER) {
            notifier = new BootstrapJmeterNotifier();
        }
        processor.setNotifier(notifier);
        processor.process();
    }

    /**
     * 打印消息
     *
     * @param message
     */
    private static void info(String message) {
        System.out.println(message);
    }

    /**
     * 打印错误
     *
     * @param message
     */
    private static void error(String message) {
        System.err.println("ERROR " + message);
    }

}
