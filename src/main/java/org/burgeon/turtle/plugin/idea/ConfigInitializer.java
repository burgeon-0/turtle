package org.burgeon.turtle.plugin.idea;

import com.intellij.openapi.project.Project;
import org.burgeon.turtle.core.common.Constants;

/**
 * 配置初始化程序
 *
 * @author luxiaocong
 * @createdOn 2021/4/16
 */
public class ConfigInitializer {

    /**
     * 初始化环境配置
     *
     * @param project
     */
    public static void init(Project project) {
        if (System.getenv(Constants.TURTLE_HOME) == null) {
            NotificationHelper.error(project, "配置错误", "请配置Turtle运行环境");
            return;
        }

        System.setProperty(Constants.CONF_PATH, Constants.CONF_DIR);
        String currentPath = project.getBasePath();
        System.setProperty(Constants.SOURCE_PATH, currentPath);
        System.setProperty(Constants.TARGET_PATH, currentPath + Constants.OUT_DIR);
    }

}
