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
     * 项目名
     */
    public static final String PROJECT_NAME = "turtle";

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
     * 左大括号
     */
    public static final String LEFT_BRACE = "{";
    /**
     * 右大括号
     */
    public static final String RIGHT_BRACE = "}";
    /**
     * 左中括号
     */
    public static final String LEFT_BRACKET = "[";
    /**
     * 右中括号
     */
    public static final String RIGHT_BRACKET = "]";
    /**
     * 左小括号
     */
    public static final String LEFT_PARENTHESES = "(";
    /**
     * 右小括号
     */
    public static final String RIGHT_PARENTHESES = ")";

    /**
     * 加号
     */
    public static final String PLUS = "+";
    /**
     * 减号
     */
    public static final String MINUS = "-";
    /**
     * 等于号
     */
    public static final String EQUAL = "=";
    /**
     * 下划线
     */
    public static final String UNDERLINE = "_";

    /**
     * turtle安装目录key
     */
    public static final String TURTLE_HOME = "TURTLE_HOME";

    public static final String BIN_DIR = SEPARATOR_FILE + "bin";
    public static final String CONF_DIR = SEPARATOR_FILE + "conf";
    public static final String OUT_DIR = SEPARATOR_FILE + "out";
    public static final String SPOON_DIR = SEPARATOR_FILE + "spooned";

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

    /**
     * HTTP Content-Type
     */
    public static final String CONTENT_TYPE = "Content-Type";

}
