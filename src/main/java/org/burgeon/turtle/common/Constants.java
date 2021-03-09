package org.burgeon.turtle.common;

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

    /**
     * 配置key
     */
    public static final String CONF_PATH = "confpath";
    public static final String DEBUG = "debug";
    public static final String COMPILE_ORDER = "compile.order";
    public static final String COMPILE_GUARANTEE = "compile.guarantee";
    public static final String COMPILE_MODE = "compile.mode";
    public static final String CUSTOM_SOURCE_PATH = "custom.sourcepath";
    public static final String CUSTOM_TARGET_PATH = "custom.targetpath";
    public static final String CUSTOM_CLASSPATH = "custom.classpath";

    /**
     * 默认配置，在配置文件加载不到的时候应用，如：作为IDEA插件的时候
     */
    public static final String[] DEFALUT_COMPILE_ORDER = new String[]{"maven", "gradle", "custom"};
    public static final String DEFALUT_COMPILE_GUARANTEE = "can_skip";

    /**
     * 逗号分隔符
     */
    public static final String SEPARATOR_COMMA = ",";

}
