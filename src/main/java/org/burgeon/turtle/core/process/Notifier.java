package org.burgeon.turtle.core.process;

import org.burgeon.turtle.core.model.api.ApiProject;
import org.burgeon.turtle.core.event.ExportEvent;

/**
 * 通知器
 *
 * @author luxiaocong
 * @createdOn 2021/2/27
 */
public interface Notifier {

    /**
     * 通知器将结果通知出来，供导出器处理
     *
     * @param apiProject
     * @return
     */
    ExportEvent notice(ApiProject apiProject);

}
