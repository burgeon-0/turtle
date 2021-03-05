package org.burgeon.turtle.export;

import org.burgeon.turtle.core.event.ExportEventSupport;
import org.burgeon.turtle.export.blueprint.ApiBlueprintExporter;

/**
 * 导出器默认配置
 *
 * @author luxiaocong
 * @createdOn 2021/3/5
 */
public class DefaultConfig {

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
