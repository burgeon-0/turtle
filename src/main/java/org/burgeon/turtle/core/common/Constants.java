package org.burgeon.turtle.core.common;

import java.io.File;

/**
 * 常量
 *
 * @author luxiaocong
 * @createdOn 2021/3/4
 */
public class Constants {

    /**
     * 空格分隔符
     */
    public static final String SEPARATOR_SPACE = " ";
    /**
     * 冒号分隔符
     */
    public static final String SEPARATOR_COLON = ":";
    /**
     * 逗号分隔符
     */
    public static final String SEPARATOR_COMMA = ",";
    /**
     * 分号分隔符
     */
    public static final String SEPARATOR_SEMICOLON = ";";
    /**
     * 目录分隔符
     */
    public static final String SEPARATOR_FILE = File.separator;
    /**
     * 系统换行符
     */
    public static final String SEPARATOR_LINE_BREAK = System.lineSeparator();

    /**
     * turtle安装目录key
     */
    public static final String TURTLE_HOME = "TURTLE_HOME";
    public static final String BIN_DIR = SEPARATOR_FILE + "bin";
    public static final String CONF_DIR = SEPARATOR_FILE + "conf";
    public static final String OUT_DIR = SEPARATOR_FILE + "out";

    /**
     * 配置key
     */
    public static final String CONF_PATH = "confpath";
    public static final String DEBUG = "debug";
    public static final String ANALYSIS_ORDER = "analysis.order";
    public static final String ANALYSIS_MODE = "analysis.mode";
    public static final String SOURCE_PATH = "sourcepath";
    public static final String TARGET_PATH = "targetpath";
    public static final String CLASSPATH = "classpath";

    /**
     * 默认配置，在配置文件加载不到的时候应用，如：作为IDEA插件的时候
     */
    public static final String[] DEFAULT_ANALYSIS_ORDER = new String[]{"maven", "gradle", "custom"};

}
