package org.burgeon.turtle.common;

import java.io.File;

/**
 * 常量
 *
 * @author luxiaocong
 * @createdOn 2021/3/4
 */
public class Constants {

    /**
     * turtle安装目录key
     */
    public static final String TURTLE_HOME = "TURTLE_HOME";
    public static final String BIN_DIR = File.separator + "bin";
    public static final String CONF_DIR = File.separator + "conf";
    public static final String OUT_DIR = File.separator + "out";

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
     * 逗号分隔符
     */
    public static final String SEPARATOR_COMMA = ",";

}
