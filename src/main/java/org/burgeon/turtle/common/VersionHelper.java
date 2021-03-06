package org.burgeon.turtle.common;

import lombok.extern.slf4j.Slf4j;

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
        InputStream inputStream = VersionHelper.class.getClassLoader().getResourceAsStream("version");
        Properties versionProperties = new Properties();
        try {
            versionProperties.load(inputStream);
        } catch (Exception e) {
            log.error("Could not load version file.", e);
        }
        VERSION = versionProperties.getProperty("version", "unknown");
    }

}
