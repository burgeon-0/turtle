package org.burgeon.turtle.core.utils;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;

/**
 * 系统环境变量工具类
 *
 * @author luxiaocong
 * @createdOn 2021/4/17
 */
public class SystemEnvUtils {

    /**
     * 重新设置系统环境变量
     *
     * @param newEnv
     * @throws Exception
     */
    public static void setEnv(Map<String, String> newEnv) throws Exception {
        try {
            Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
            Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
            theEnvironmentField.setAccessible(true);
            Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
            env.putAll(newEnv);
            Field theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment");
            theCaseInsensitiveEnvironmentField.setAccessible(true);
            Map<String, String> ciEnv = (Map<String, String>) theCaseInsensitiveEnvironmentField.get(null);
            ciEnv.putAll(newEnv);
        } catch (NoSuchFieldException e) {
            Class[] classes = Collections.class.getDeclaredClasses();
            Map<String, String> env = System.getenv();
            for (Class cl : classes) {
                if ("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
                    Field field = cl.getDeclaredField("m");
                    field.setAccessible(true);
                    Object obj = field.get(env);
                    Map<String, String> map = (Map<String, String>) obj;
                    map.clear();
                    map.putAll(newEnv);
                }
            }
        }
    }

}
