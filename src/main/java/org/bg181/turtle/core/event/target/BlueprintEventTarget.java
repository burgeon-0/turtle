package org.bg181.turtle.core.event.target;

/**
 * 事件目标：API Blueprint
 *
 * @author Sam Lu
 * @createdOn 2021/4/19
 */
public class BlueprintEventTarget extends EventTarget {

    @Override
    public int getCode() {
        return 301;
    }

    @Override
    public String getName() {
        return "blueprint";
    }

}
