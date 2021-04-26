package org.burgeon.turtle.core.event.source;

/**
 * 事件来源：IntelliJ IDEA
 *
 * @author Sam Lu
 * @createdOn 2021/4/19
 */
public class IdeaEventSource extends EventSource {

    @Override
    public int getCode() {
        return 101;
    }

    @Override
    public String getName() {
        return "idea";
    }

}
