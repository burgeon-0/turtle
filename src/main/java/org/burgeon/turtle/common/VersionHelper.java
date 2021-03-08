package org.burgeon.turtle.common;

import lombok.extern.slf4j.Slf4j;
import org.burgeon.turtle.utils.EnvUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Version工具
 *
 * @author luxiaocong
 * @createdOn 2021/3/5
 */
@Slf4j
public class VersionHelper {

    public static final String VERSION;

    // version file path: ./build/resources/main/conf/version or ./conf/version
    static {
        String filePath = EnvUtils.getStringProperty(Constants.TURTLE_HOME)
                + EnvUtils.getStringProperty(Constants.CONF_PATH) + "/version";
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            log.error("File not found: {}.", filePath);
        }

        Properties versionProperties = new Properties();
        try {
            versionProperties.load(inputStream);
        } catch (Exception e) {
            log.error("Could not load version file: {}.", filePath);
        }

        VERSION = versionProperties.getProperty("version", "unknown");
    }

}
