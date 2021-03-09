package org.burgeon.turtle.common;

import java.util.Properties;

/**
 * 配置初始化器
 *
 * @author luxiaocong
 * @createdOn 2021/3/9
 */
public class PropertiesInitializer {

    /**
     * 初始化项目配置
     */
    public static void init() {
        // conf file path: turtle/build/resources/main/conf/turtle.conf or turtle/conf/turtle.conf
        Properties properties = PropertiesLoader.loadProperties("turtle.conf");

        // init compile config
        if (properties.getProperty(Constants.COMPILE_ORDER) != null) {
            System.setProperty(Constants.COMPILE_ORDER, properties.getProperty(Constants.COMPILE_ORDER));
        }
        if (properties.getProperty(Constants.COMPILE_GUARANTEE) != null) {
            System.setProperty(Constants.COMPILE_GUARANTEE, properties.getProperty(Constants.COMPILE_GUARANTEE));
        }
        if (properties.getProperty(Constants.COMPILE_MODE) != null) {
            System.setProperty(Constants.COMPILE_MODE, properties.getProperty(Constants.COMPILE_MODE));
        }

        // init custom config
        if (properties.getProperty(Constants.CUSTOM_SOURCE_PATH) != null) {
            System.setProperty(Constants.CUSTOM_SOURCE_PATH, properties.getProperty(Constants.CUSTOM_SOURCE_PATH));
        }
        if (properties.getProperty(Constants.CUSTOM_TARGET_PATH) != null) {
            System.setProperty(Constants.CUSTOM_TARGET_PATH, properties.getProperty(Constants.CUSTOM_TARGET_PATH));
        }
        if (properties.getProperty(Constants.CUSTOM_CLASSPATH) != null) {
            System.setProperty(Constants.CUSTOM_CLASSPATH, properties.getProperty(Constants.CUSTOM_CLASSPATH));
        }
    }

}
