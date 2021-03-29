package org.burgeon.turtle.core.common;

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

    /**
     * 从gradle配置指定的版本信息文件中加载项目版本
     */
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
