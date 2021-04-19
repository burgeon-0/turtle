package org.burgeon.turtle.bootstrap.notifier;

import org.burgeon.turtle.core.event.ExportEvent;
import org.burgeon.turtle.core.event.source.BootstrapEventSource;
import org.burgeon.turtle.core.event.source.EventSource;
import org.burgeon.turtle.core.event.target.BlueprintEventTarget;
import org.burgeon.turtle.core.event.target.EventTarget;
import org.burgeon.turtle.core.model.api.ApiProject;
import org.burgeon.turtle.core.process.Notifier;

/**
 * Bootstrap - API Blueprint通知器
 *
 * @author luxiaocong
 * @createdOn 2021/3/4
 */
public class BootstrapApiBlueprintNotifier implements Notifier {

    /**
     * 命令行方式触发的API Blueprint文档导出事件
     *
     * @param apiProject
     * @return
     */
    @Override
    public ExportEvent notice(ApiProject apiProject) {
        ExportEvent exportEvent = new ExportEvent();
        exportEvent.setSourceCode(EventSource.fromClass(BootstrapEventSource.class).getCode());
        exportEvent.setTargetCodes(new int[]{EventTarget.fromClass(BlueprintEventTarget.class).getCode()});
        exportEvent.setApiProject(apiProject);
        return exportEvent;
    }

}
