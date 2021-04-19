package org.burgeon.turtle.core.event.source;

/**
 * 事件来源：Eclipse
 *
 * @author luxiaocong
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
