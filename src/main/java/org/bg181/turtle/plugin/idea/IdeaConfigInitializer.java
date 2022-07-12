package org.bg181.turtle.plugin.idea;

import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.openapi.project.Project;
import org.bg181.turtle.core.common.Constants;
import org.bg181.turtle.core.utils.SystemEnvUtils;

import java.util.Map;

/**
 * 配置初始化程序
 *
 * @author Sam Lu
 * @createdOn 2021/4/16
 */
public class IdeaConfigInitializer {

    /**
     * 初始化环境配置
     *
     * @param project
     */
    public static synchronized void init(Project project) {
        Map<String, String> parentEnvironment = new GeneralCommandLine().getParentEnvironment();
        if (parentEnvironment.get(Constants.TURTLE_HOME) == null) {
            NotificationHelper.error(project, "配置错误", "请配置Turtle运行环境");
        }

        System.setProperty(Constants.TURTLE_HOME, parentEnvironment.get(Constants.TURTLE_HOME));
        System.setProperty(Constants.CONF_PATH, Constants.CONF_DIR);
        SystemEnvUtils.setenv(Constants.M2_HOME, parentEnvironment.get(Constants.M2_HOME));
    }

}
