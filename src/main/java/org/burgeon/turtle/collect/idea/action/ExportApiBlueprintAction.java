package org.burgeon.turtle.collect.idea.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import lombok.extern.slf4j.Slf4j;
import org.burgeon.turtle.collect.idea.process.ApiBlueprintProcessor;
import org.burgeon.turtle.core.event.ExportEventSupport;
import org.burgeon.turtle.core.process.Processor;
import org.burgeon.turtle.export.blueprint.ApiBlueprintExporter;

/**
 * 菜单[导出API Blueprint文档]
 *
 * @author luxiaocong
 * @createdOn 2021/2/10
 */
@Slf4j
public class ExportApiBlueprintAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        log.debug("Receive an [Export API Blueprint] action.");
        Processor processor = new ApiBlueprintProcessor();
        processor.process();
    }

    @Override
    public void update(AnActionEvent e) {
        // 将导出事件监听器添加到支撑组件中
        if (!ExportEventSupport.findExportListener().contains(ApiBlueprintExporter.getInstance())) {
            ExportEventSupport.addExportListener(ApiBlueprintExporter.getInstance());
        }
    }

}
