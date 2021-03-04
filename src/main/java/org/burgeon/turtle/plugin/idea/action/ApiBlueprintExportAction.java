package org.burgeon.turtle.plugin.idea.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import lombok.extern.slf4j.Slf4j;
import org.burgeon.turtle.plugin.idea.config.ExporterConfig;
import org.burgeon.turtle.plugin.idea.process.ApiBlueprintNotifier;
import org.burgeon.turtle.core.process.DefaultProcessor;
import org.burgeon.turtle.core.process.Notifier;
import org.burgeon.turtle.core.process.Processor;

/**
 * 菜单[导出API Blueprint文档]
 *
 * @author luxiaocong
 * @createdOn 2021/2/10
 */
@Slf4j
public class ApiBlueprintExportAction extends AnAction {

    /**
     * 每次调出菜单的时候触发
     *
     * @param e
     */
    @Override
    public void update(AnActionEvent e) {
        ExporterConfig exporterConfig = new ExporterConfig();
        exporterConfig.init();
    }

    /**
     * 点击菜单的时候触发
     *
     * @param e
     */
    @Override
    public void actionPerformed(AnActionEvent e) {
        log.info("Receive an [Export API Blueprint] action.");

        // 进行处理
        Processor processor = new DefaultProcessor();
        Notifier notifier = new ApiBlueprintNotifier();
        processor.setNotifier(notifier);
        processor.process();
    }

}
