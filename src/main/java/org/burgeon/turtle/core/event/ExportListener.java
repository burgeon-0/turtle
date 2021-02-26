package org.burgeon.turtle.core.event;

/**
 * 导出监听器
 *
 * @author luxiaocong
 * @createdOn 2021/2/26
 */
public abstract class ExportListener {

    /**
     * 发生导出事件
     *
     * @param exportEvent
     */
    public abstract void action(ExportEvent exportEvent);

}
