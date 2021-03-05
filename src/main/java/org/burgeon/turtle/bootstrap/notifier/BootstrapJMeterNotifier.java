package org.burgeon.turtle.bootstrap.notifier;

import org.burgeon.turtle.core.event.EventSource;
import org.burgeon.turtle.core.event.EventTarget;
import org.burgeon.turtle.core.event.ExportEvent;
import org.burgeon.turtle.core.model.api.ApiProject;
import org.burgeon.turtle.core.process.Notifier;

/**
 * Bootstrap - Apache JMeter通知器
 *
 * @author luxiaocong
 * @createdOn 2021/3/4
 */
public class BootstrapJMeterNotifier implements Notifier {

    @Override
    public ExportEvent notice(ApiProject apiProject) {
        ExportEvent exportEvent = new ExportEvent();
        exportEvent.setSourceCode(EventSource.BOOTSTRAP.getCode());
        exportEvent.setTargetCodes(new int[]{EventTarget.JMETER.getCode()});
        exportEvent.setApiProject(apiProject);
        return exportEvent;
    }

}
