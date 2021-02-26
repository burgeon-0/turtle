package org.burgeon.turtle.core.event;

import java.util.ArrayList;
import java.util.List;

/**
 * 导出事件支撑组件
 *
 * @author luxiaocong
 * @createdOn 2021/2/26
 */
public class ExportEventSupport {

    private static List<ExportListener> exportListeners = new ArrayList<>();

    /**
     * 查询导出事件监听器
     *
     * @return
     */
    public static List<ExportListener> findExportListener() {
        return exportListeners;
    }

    /**
     * 添加导出事件监听器
     *
     * @param exportListener
     */
    public static void addExportListener(ExportListener exportListener) {
        exportListeners.add(exportListener);
    }

    /**
     * 移除导出事件监听器
     *
     * @param exportListener
     */
    public static void removeExportListener(ExportListener exportListener) {
        exportListeners.remove(exportListener);
    }

    /**
     * 触发导出事件
     *
     * @param exportEvent
     */
    public static void fireExportEvent(ExportEvent exportEvent) {
        for (ExportListener exportListener : exportListeners) {
            exportListener.action(exportEvent);
        }
    }

}
