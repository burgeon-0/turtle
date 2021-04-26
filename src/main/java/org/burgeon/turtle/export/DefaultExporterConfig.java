package org.burgeon.turtle.export;

import org.burgeon.turtle.core.event.ExportEventSupport;
import org.burgeon.turtle.core.event.source.BootstrapEventSource;
import org.burgeon.turtle.core.event.source.EclipseEventSource;
import org.burgeon.turtle.core.event.source.IdeaEventSource;
import org.burgeon.turtle.core.event.source.VsCodeEventSource;
import org.burgeon.turtle.core.event.target.BlueprintEventTarget;
import org.burgeon.turtle.core.event.target.JMeterEventTarget;
import org.burgeon.turtle.core.event.target.PostmanEventTarget;
import org.burgeon.turtle.export.blueprint.ApiBlueprintExporter;

/**
 * 导出器默认配置
 *
 * @author Sam Lu
 * @createdOn 2021/3/5
 */
public class DefaultExporterConfig {

    /**
     * 初始化配置
     */
    public synchronized void init() {
        // 预先加载事件来源和事件目标
        new BootstrapEventSource();
        new IdeaEventSource();
        new EclipseEventSource();
        new VsCodeEventSource();
        new BlueprintEventTarget();
        new PostmanEventTarget();
        new JMeterEventTarget();

        // 将导出事件监听器添加到事件管理器中
        if (!ExportEventSupport.contains(ApiBlueprintExporter.getInstance())) {
            ExportEventSupport.addExportListener(ApiBlueprintExporter.getInstance());
        }
    }

}
