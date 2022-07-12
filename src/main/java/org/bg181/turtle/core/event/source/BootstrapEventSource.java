package org.bg181.turtle.core.event.source;

/**
 * 事件来源：Bootstrap
 *
 * @author Sam Lu
 * @createdOn 2021/4/19
 */
public class BootstrapEventSource extends EventSource {

    @Override
    public int getCode() {
        return 100;
    }

    @Override
    public String getName() {
        return "bootstrap";
    }

}
