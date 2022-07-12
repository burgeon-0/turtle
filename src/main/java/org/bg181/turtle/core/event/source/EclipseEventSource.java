package org.bg181.turtle.core.event.source;

/**
 * 事件来源：Eclipse
 *
 * @author Sam Lu
 * @createdOn 2021/4/19
 */
public class EclipseEventSource extends EventSource {

    @Override
    public int getCode() {
        return 102;
    }

    @Override
    public String getName() {
        return "eclipse";
    }

}
