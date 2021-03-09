package org.burgeon.turtle.common;

import lombok.extern.slf4j.Slf4j;

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
        // version file path: turtle/build/resources/main/conf/version or turtle/conf/version
        Properties versionProperties = PropertiesLoader.loadProperties("version");
        VERSION = versionProperties.getProperty("version", "unknown");
    }

}
