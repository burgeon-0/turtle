package org.burgeon.turtle.collect.idea.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import lombok.extern.slf4j.Slf4j;
import org.burgeon.turtle.collect.idea.config.ExporterConfig;

/**
 * 菜单[API]
 *
 * @author luxiaocong
 * @createdOn 2021/2/10
 */
@Slf4j
public class ApiActionGroup extends DefaultActionGroup {

    @Override
    public void actionPerformed(AnActionEvent e) {
        log.debug("Receive an [API] action.");
    }

    @Override
    public void update(AnActionEvent e) {
        ExporterConfig exporterConfig = new ExporterConfig();
        exporterConfig.init();
    }

}
