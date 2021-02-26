package org.burgeon.turtle.collect.idea.process;

import org.burgeon.turtle.core.event.DefaultCodes;
import org.burgeon.turtle.core.event.ExportEvent;
import org.burgeon.turtle.core.model.Project;
import org.burgeon.turtle.core.process.Processor;

/**
 * API Blueprint处理器
 *
 * @author luxiaocong
 * @createdOn 2021/2/26
 */
public class ApiBlueprintProcessor extends Processor {

    @Override
    protected ExportEvent internalProcess(Project project) {
        ExportEvent exportEvent = new ExportEvent();
        exportEvent.setSourceCode(DefaultCodes.SOURCE_CODE_IDEA);
        exportEvent.setTargetCodes(new int[]{DefaultCodes.TARGET_CODE_BLUEPRINT});
        exportEvent.setProject(project);
        return exportEvent;
    }

}
