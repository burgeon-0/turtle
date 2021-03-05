package org.burgeon.turtle.core.event;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 事件来源
 *
 * @author luxiaocong
 * @createdOn 2021/3/4
 */
public enum EventSource {

    /**
     * 事件来源：Bootstrap
     */
    BOOTSTRAP(100, "bootstrap"),
    /**
     * 事件来源：IntelliJ IDEA
     */
    IDEA(101, "idea"),
    /**
     * 事件来源：Eclipse
     */
    ECLIPSE(102, "eclipse"),
    /**
     * 事件来源：Visual Studio Code
     */
    VSCODE(103, "vscode");

    @Getter
    private int code;
    private String name;

    private static final Map<String, EventSource> valuesMap = new HashMap(values().length);

    static {
        for (EventSource eventSource : values()) {
            valuesMap.put(eventSource.name, eventSource);
        }
    }

    EventSource(int code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 是否包含该EventSource
     *
     * @param name
     * @return
     */
    public static boolean contains(String name) {
        EventSource eventSource = valuesMap.get(name);
        if (eventSource != null) {
            return true;
        }
        return false;
    }

    /**
     * 通过name获取EventSource
     *
     * @param name
     * @return
     */
    public static EventSource fromName(String name) {
        return valuesMap.get(name);
    }

}
