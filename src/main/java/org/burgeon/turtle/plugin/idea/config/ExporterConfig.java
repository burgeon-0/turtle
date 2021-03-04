package org.burgeon.turtle.plugin.idea.config;

import org.burgeon.turtle.core.event.ExportEventSupport;
import org.burgeon.turtle.export.blueprint.ApiBlueprintExporter;

/**
 * 导出器配置
 *
 * @author luxiaocong
 * @createdOn 2021/2/26
 */
public class ExporterConfig {

    /**
     * 初始化配置
     */
    public synchronized void init() {
        // 将导出事件监听器添加到事件管理器中
        if (!ExportEventSupport.contains(ApiBlueprintExporter.getInstance())) {
            ExportEventSupport.addExportListener(ApiBlueprintExporter.getInstance());
        }
    }

}
