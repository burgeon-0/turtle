package org.burgeon.turtle.core.event;

import java.util.ArrayList;
import java.util.List;

/**
 * 导出事件管理器
 *
 * @author luxiaocong
 * @createdOn 2021/2/26
 */
public class ExportEventSupport {

    private static List<ExportListener> exportListeners = new ArrayList<>();

    /**
     * 添加导出事件监听器
     *
     * @param exportListener
     */
    public static synchronized void addExportListener(ExportListener exportListener) {
        exportListeners.add(exportListener);
    }

    /**
     * 移除导出事件监听器
     *
     * @param exportListener
     */
    public static synchronized void removeExportListener(ExportListener exportListener) {
        exportListeners.remove(exportListener);
    }

    /**
     * 查询导出事件监听器
     *
     * @return
     */
    public static List<ExportListener> getExportListeners() {
        return exportListeners;
    }

    /**
     * 判断是否已经包含该事件监听器
     *
     * @param exportListener
     * @return
     */
    public static boolean contains(ExportListener exportListener) {
        if (exportListeners.contains(exportListener)) {
            return true;
        }
        return false;
    }

    /**
     * 触发导出事件
     *
     * @param exportEvent
     */
    public static void fireExportEvent(ExportEvent exportEvent) {
        for (ExportListener exportListener : exportListeners) {
            exportListener.dispatch(exportEvent);
        }
    }

}
