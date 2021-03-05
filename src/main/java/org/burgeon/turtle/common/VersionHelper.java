package org.burgeon.turtle.common;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
        InputStream inputStream = VersionHelper.class.getClassLoader()
                .getResourceAsStream("classpath:/version.properties");
        if (inputStream == null) {
            // When running unit tests, no jar is built, so we load a copy of the file that we saved during build.gradle.
            // Possibly this also is the case during debugging, therefore we save in bin/main instead of bin/test.
            try {
                inputStream = new FileInputStream("build/resources/main/version.properties");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        try {
            versionProperties.load(inputStream);
        } catch (IOException e) {
            log.error("Could not load classpath:/version.properties", e);
        }

        VERSION = versionProperties.getProperty("version", "unknown");
    }

}
