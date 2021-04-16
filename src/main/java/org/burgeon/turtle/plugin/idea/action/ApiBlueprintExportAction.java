package org.burgeon.turtle.plugin.idea.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
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

import java.util.concurrent.*;

/**
 * 菜单[导出API Blueprint文档]
 *
 * @author luxiaocong
 * @createdOn 2021/2/10
 */
@Slf4j
public class ApiBlueprintExportAction extends AnAction {

    private DefaultExporterConfig exporterConfig = new DefaultExporterConfig();
    private Boolean init = false;

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

        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());
        executor.submit(() -> {
            // 进行处理
            Processor processor = new DefaultProcessor();
            Notifier notifier = new IdeaApiBlueprintNotifier();
            processor.setNotifier(notifier);
            processor.process();

            String targetPath = EnvUtils.getStringProperty(Constants.TARGET_PATH);
            NotificationHelper.info(e.getProject(), "导出成功", "API文档已导出到：" + targetPath);
        });
        executor.shutdown();
    }

}
