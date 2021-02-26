package org.burgeon.turtle.core.process;

import org.burgeon.turtle.core.event.ExportEvent;
import org.burgeon.turtle.core.event.ExportEventSupport;
import org.burgeon.turtle.core.model.Project;

/**
 * 处理器
 *
 * @author luxiaocong
 * @createdOn 2021/2/26
 */
public abstract class Processor {

    /**
     * 项目
     */
    private Project project;

    /**
     * 处理
     *
     * @param project
     * @return
     */
    protected abstract ExportEvent internalProcess(Project project);

    /**
     * 处理
     */
    public void process() {
        if (project == null) {
            project = new Project();
        }
        ExportEvent exportEvent = internalProcess(project);
        ExportEventSupport.fireExportEvent(exportEvent);
        project = null;
    }

}
