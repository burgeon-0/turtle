package org.burgeon.turtle.core.common;

import java.util.Properties;

/**
 * 配置初始化器
 *
 * @author Sam Lu
 * @createdOn 2021/3/9
 */
public class ConfigInitializer {

    /**
     * 初始化项目配置，各配置项的含义可参阅配置文件（turtle.conf）的描述
     */
    public static synchronized void init() {
        // conf file path: turtle/build/resources/main/conf/turtle.conf or turtle/conf/turtle.conf
        Properties properties = PropertiesLoader.loadProperties("turtle.conf");

        // init analysis config
        if (properties.getProperty(Constants.ANALYSIS_ORDER) != null) {
            System.setProperty(Constants.ANALYSIS_ORDER, properties.getProperty(Constants.ANALYSIS_ORDER));
        }
        if (properties.getProperty(Constants.ANALYSIS_MODE) != null) {
            System.setProperty(Constants.ANALYSIS_MODE, properties.getProperty(Constants.ANALYSIS_MODE));
        }

        // init directory config
        if (properties.getProperty(Constants.SOURCE_PATH) != null) {
            System.setProperty(Constants.SOURCE_PATH, properties.getProperty(Constants.SOURCE_PATH));
        }
        if (properties.getProperty(Constants.TARGET_PATH) != null) {
            System.setProperty(Constants.TARGET_PATH, properties.getProperty(Constants.TARGET_PATH));
        }
        if (properties.getProperty(Constants.CLASSPATH) != null) {
            System.setProperty(Constants.CLASSPATH, properties.getProperty(Constants.CLASSPATH));
        }
    }

}
