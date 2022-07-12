package org.bg181.turtle.plugin.idea;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;

/**
 * IDEA通知工具
 *
 * @author Sam Lu
 * @createdOn 2021/4/16
 */
public class NotificationHelper {

    private static final String DISPLAY_ID = "Turtle Plugin";
    private static final NotificationGroup NOTIFICATION_GROUP = new NotificationGroup(DISPLAY_ID,
            NotificationDisplayType.BALLOON, true);

    /**
     * 提示信息
     *
     * @param project
     * @param title
     * @param content
     */
    public static void info(Project project, String title, String content) {
        Notification notification = NOTIFICATION_GROUP.createNotification(title, content,
                MessageType.INFO.toNotificationType(), null);
        notification.notify(project);
    }

    /**
     * 告警信息
     *
     * @param project
     * @param title
     * @param content
     */
    public static void warn(Project project, String title, String content) {
        Notification notification = NOTIFICATION_GROUP.createNotification(title, content,
                MessageType.WARNING.toNotificationType(), null);
        notification.notify(project);
    }

    /**
     * 错误信息
     *
     * @param project
     * @param title
     * @param content
     */
    public static void error(Project project, String title, String content) {
        Notification notification = NOTIFICATION_GROUP.createNotification(title, content,
                MessageType.ERROR.toNotificationType(), null);
        notification.notify(project);
    }

}
