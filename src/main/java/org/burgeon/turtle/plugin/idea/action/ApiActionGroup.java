package org.burgeon.turtle.plugin.idea.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import lombok.extern.slf4j.Slf4j;

/**
 * 菜单[API]
 *
 * @author Sam Lu
 * @createdOn 2021/2/10
 */
@Slf4j
public class ApiActionGroup extends DefaultActionGroup {

    /**
     * 点击菜单的时候触发
     *
     * @param e
     */
    @Override
    public void actionPerformed(AnActionEvent e) {
        log.debug("Receive an [API] action.");
    }

}
