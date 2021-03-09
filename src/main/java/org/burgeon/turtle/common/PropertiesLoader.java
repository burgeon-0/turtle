package org.burgeon.turtle.common;

import lombok.extern.slf4j.Slf4j;
import org.burgeon.turtle.utils.EnvUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 配置加载器
 *
 * @author luxiaocong
 * @createdOn 2021/3/9
 */
@Slf4j
public class PropertiesLoader {

    private static final String CONF_BASE_PATH = EnvUtils.getStringProperty(Constants.TURTLE_HOME)
            + EnvUtils.getStringProperty(Constants.CONF_PATH);

    /**
     * 加载配置
     *
     * @param filename
     * @return
     */
    public static Properties loadProperties(String filename) {
        String filePath = CONF_BASE_PATH + "/" + filename;
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            log.error("File not found: {}.", filePath);
        }

        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (Exception e) {
            log.error("Could not load properties file: {}.", filePath);
        }
        return properties;
    }

}
