package org.burgeon.turtle.collect.idea.process;

import org.burgeon.turtle.core.model.api.Application;
import org.burgeon.turtle.core.event.DefaultCodes;
import org.burgeon.turtle.core.event.ExportEvent;
import org.burgeon.turtle.core.process.Notifier;

/**
 * API Blueprint通知器
 *
 * @author luxiaocong
 * @createdOn 2021/2/27
 */
public class ApiBlueprintNotifier implements Notifier {

    @Override
    public ExportEvent notice(Application application) {
        ExportEvent exportEvent = new ExportEvent();
        exportEvent.setSourceCode(DefaultCodes.SOURCE_CODE_IDEA);
        exportEvent.setTargetCodes(new int[]{DefaultCodes.TARGET_CODE_BLUEPRINT});
        exportEvent.setApplication(application);
        return exportEvent;
    }

}
