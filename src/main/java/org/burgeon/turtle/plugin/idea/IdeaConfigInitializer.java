package org.burgeon.turtle.plugin.idea;

import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.openapi.project.Project;
import org.burgeon.turtle.core.common.Constants;
import org.burgeon.turtle.core.utils.SystemEnvUtils;

import java.util.Map;

/**
 * 配置初始化程序
 *
 * @author luxiaocong
 * @createdOn 2021/4/16
 */
public class IdeaConfigInitializer {

    /**
     * 初始化环境配置
     *
     * @param project
     */
    public static synchronized void init(Project project) throws Exception {
        Map<String, String> parentEnvironment = new GeneralCommandLine().getParentEnvironment();
        if (parentEnvironment.get(Constants.TURTLE_HOME) == null) {
            NotificationHelper.error(project, "配置错误", "请配置Turtle运行环境");
        }

        SystemEnvUtils.setEnv(parentEnvironment);
        System.setProperty(Constants.TURTLE_HOME, parentEnvironment.get(Constants.TURTLE_HOME));
        System.setProperty(Constants.CONF_PATH, Constants.CONF_DIR);
    }

}
