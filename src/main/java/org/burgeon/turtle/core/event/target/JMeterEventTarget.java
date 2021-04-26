package org.burgeon.turtle.core.event.target;

/**
 * 事件目标：Apache JMeter
 *
 * @author Sam Lu
 * @createdOn 2021/4/19
 */
public class JMeterEventTarget extends EventTarget {

    @Override
    public int getCode() {
        return 303;
    }

    @Override
    public String getName() {
        return "jmeter";
    }

}
