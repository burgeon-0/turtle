package org.burgeon.turtle.core.event;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 事件目标
 *
 * @author luxiaocong
 * @createdOn 2021/3/4
 */
public enum EventTarget {

    /**
     * 事件目标：API Blueprint
     */
    BLUEPRINT(301, "blueprint"),
    /**
     * 事件编码：Postman
     */
    POSTMAN(302, "postman"),
    /**
     * 事件编码：Apache JMeter
     */
    JMETER(303, "jmeter");

    @Getter
    private int code;
    private String name;

    private static final Map<String, EventTarget> valuesMap = new HashMap(values().length);

    static {
        for (EventTarget eventTarget : values()) {
            valuesMap.put(eventTarget.name, eventTarget);
        }
    }

    EventTarget(int code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 是否包含该EventTarget
     *
     * @param name
     * @return
     */
    public static boolean contains(String name) {
        EventTarget eventTarget = valuesMap.get(name);
        if (eventTarget != null) {
            return true;
        }
        return false;
    }

    /**
     * 通过name获取EventTarget
     *
     * @param name
     * @return
     */
    public static EventTarget fromName(String name) {
        return valuesMap.get(name);
    }

}
