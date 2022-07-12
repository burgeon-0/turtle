package org.bg181.turtle.core.event;

import lombok.extern.slf4j.Slf4j;

/**
 * 导出监听器
 *
 * @author Sam Lu
 * @createdOn 2021/2/26
 */
@Slf4j
public abstract class BaseExportListener {

    /**
     * 适配的目标编码
     *
     * @return
     */
    public abstract int targetCode();

    /**
     * 发生导出事件
     *
     * @param exportEvent
     */
    public abstract void action(ExportEvent exportEvent);

    /**
     * 分发事件
     *
     * @param exportEvent
     */
    public void dispatch(ExportEvent exportEvent) {
        log.debug("Receive an export event: from {} to {}.",
                exportEvent.getSourceCode(), exportEvent.getTargetCodes());
        if (exportEvent.containsTargetCode(targetCode())) {
            log.debug("Dispatch to {}.", targetCode());
            action(exportEvent);
        }
    }

}
