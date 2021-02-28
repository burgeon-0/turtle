package org.burgeon.turtle.collect.idea.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import lombok.extern.slf4j.Slf4j;
import org.burgeon.turtle.collect.DefaultCollector;
import org.burgeon.turtle.collect.idea.config.ExporterConfig;
import org.burgeon.turtle.collect.idea.process.ApiBlueprintNotifier;
import org.burgeon.turtle.collect.idea.process.IdeaProjectAnalyser;
import org.burgeon.turtle.core.process.Analyser;
import org.burgeon.turtle.core.process.Collector;
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

        // 获取项目
        Project project = AnAction.getEventProject(e);
        if (project == null) {
            log.warn("Terminate! Because of project is null.");
            return;
        }

        // 获取虚拟文件
        VirtualFile virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE);
        if (virtualFile == null || !virtualFile.isValid()) {
            log.warn("Terminate! Because of virtualFile is null or invalid.");
            return;
        }

        // 进行处理
        Processor processor = new Processor();
        Analyser analyser = new IdeaProjectAnalyser(project, virtualFile);
        processor.setAnalyser(analyser);
        Collector collector = new DefaultCollector();
        processor.addCollector(collector);
        Notifier notifier = new ApiBlueprintNotifier();
        processor.setNotifier(notifier);
        processor.process();
    }

}
