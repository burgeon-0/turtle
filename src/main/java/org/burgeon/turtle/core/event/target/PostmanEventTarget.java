package org.burgeon.turtle.core.event.target;

/**
 * 事件目标：Postman
 *
 * @author Sam Lu
 * @createdOn 2021/4/19
 */
public class PostmanEventTarget extends EventTarget {

    @Override
    public int getCode() {
        return 302;
    }

    @Override
    public String getName() {
        return "postman";
    }

}
