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

    static {
        Properties versionProperties = new Properties();
        InputStream inputStream = null;
        if (EnvUtils.getBooleanProperty(Constants.DEBUG, false)) {
            try {
                inputStream = new FileInputStream("build/resources/main/conf/version");
            } catch (FileNotFoundException e) {
                log.error("File not found: turtle/build/resources/main/conf/version.");
            }
        } else {
            try {
                inputStream = new FileInputStream("../conf/version");
            } catch (FileNotFoundException e) {
                log.error("File not found: turtle/conf/version.");
            }
        }

        try {
            versionProperties.load(inputStream);
        } catch (Exception e) {
            log.error("Could not load version file.");
        }

        VERSION = versionProperties.getProperty("version", "unknown");
    }

}
