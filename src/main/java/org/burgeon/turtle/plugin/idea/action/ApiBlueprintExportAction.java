package org.burgeon.turtle.plugin.idea.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.progress.impl.BackgroundableProcessIndicator;
import lombok.extern.slf4j.Slf4j;
import org.burgeon.turtle.core.common.Constants;
import org.burgeon.turtle.core.process.DefaultProcessor;
import org.burgeon.turtle.core.process.Notifier;
import org.burgeon.turtle.core.process.Processor;
import org.burgeon.turtle.core.utils.EnvUtils;
import org.burgeon.turtle.export.DefaultExporterConfig;
import org.burgeon.turtle.plugin.idea.ConfigInitializer;
import org.burgeon.turtle.plugin.idea.NotificationHelper;
import org.burgeon.turtle.plugin.idea.notifier.IdeaApiBlueprintNotifier;
import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 菜单[导出API Blueprint文档]
 *
 * @author luxiaocong
 * @createdOn 2021/2/10
 */
@Slf4j
public class ApiBlueprintExportAction extends AnAction {

    private DefaultExporterConfig exporterConfig = new DefaultExporterConfig();
    private volatile boolean init;
    private volatile boolean running;

    /**
     * 每次调出菜单的时候触发
     *
     * @param e
     */
    @Override
    public void update(AnActionEvent e) {
        if (!init) {
            ConfigInitializer.init(e.getProject());
            exporterConfig.init();
            init = true;
        }
    }

    /**
     * 点击菜单的时候触发
     *
     * @param e
     */
    @Override
    public void actionPerformed(AnActionEvent e) {
        log.debug("Receive an [Export API Blueprint] action.");

        if (!running) {
            running = true;

            Task.Backgroundable task = new Task.Backgroundable(e.getProject(), "正在导出") {
                @Override
                public void run(@NotNull ProgressIndicator indicator) {
                    try {
                        indicator.setText("正在导出API文档，请稍后...");

                        // 进行处理
                        Processor processor = new DefaultProcessor();
                        Notifier notifier = new IdeaApiBlueprintNotifier();
                        processor.setNotifier(notifier);
                        processor.process();

                        String targetPath = EnvUtils.getStringProperty(Constants.TARGET_PATH);
                        NotificationHelper.info(e.getProject(), "导出成功", "API文档已导出到：" + targetPath);
                    } catch (Exception ex) {
                        ex.printStackTrace();

                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        ex.printStackTrace(pw);
                        NotificationHelper.error(e.getProject(), "导出失败", sw.toString());
                    } finally {
                        running = false;
                    }
                }
            };
            ProgressManager.getInstance().runProcessWithProgressAsynchronously(task,
                    new BackgroundableProcessIndicator(task));
        } else {
            NotificationHelper.info(e.getProject(), "正在导出", "正在导出API文档，请稍后");
        }
    }

}
