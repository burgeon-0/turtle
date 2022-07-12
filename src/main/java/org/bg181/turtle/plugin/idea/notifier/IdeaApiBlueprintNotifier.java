package org.bg181.turtle.plugin.idea.notifier;

import org.bg181.turtle.core.event.ExportEvent;
import org.bg181.turtle.core.event.source.EventSource;
import org.bg181.turtle.core.event.source.IdeaEventSource;
import org.bg181.turtle.core.event.target.BlueprintEventTarget;
import org.bg181.turtle.core.event.target.EventTarget;
import org.bg181.turtle.core.model.api.ApiProject;
import org.bg181.turtle.core.process.Notifier;

/**
 * IDEA - API Blueprint通知器
 *
 * @author Sam Lu
 * @createdOn 2021/2/27
 */
public class IdeaApiBlueprintNotifier implements Notifier {

    /**
     * IntelliJ IDEA触发的API Blueprint文档导出事件
     *
     * @param apiProject
     * @return
     */
    @Override
    public ExportEvent notice(ApiProject apiProject) {
        ExportEvent exportEvent = new ExportEvent();
        exportEvent.setSourceCode(EventSource.fromClass(IdeaEventSource.class).getCode());
        exportEvent.setTargetCodes(new int[]{EventTarget.fromClass(BlueprintEventTarget.class).getCode()});
        exportEvent.setApiProject(apiProject);
        return exportEvent;
    }

}
