package org.burgeon.turtle.plugin.idea;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;

/**
 * IDEA通知工具
 *
 * @author luxiaocong
 * @createdOn 2021/4/16
 */
public class NotificationHelper {

    private static final String DISPLAY_ID = "Turtle Plugin";

    /**
     * 提示信息
     *
     * @param project
     * @param title
     * @param content
     */
    public static void info(Project project, String title, String content) {
        Notification notification = new NotificationGroup(DISPLAY_ID, NotificationDisplayType.BALLOON, true)
                .createNotification(title, content, MessageType.INFO.toNotificationType(), null);
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
        Notification notification = new NotificationGroup(DISPLAY_ID, NotificationDisplayType.BALLOON, true)
                .createNotification(title, content, MessageType.WARNING.toNotificationType(), null);
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
        Notification notification = new NotificationGroup(DISPLAY_ID, NotificationDisplayType.BALLOON, true)
                .createNotification(title, content, MessageType.ERROR.toNotificationType(), null);
        notification.notify(project);
    }

}
