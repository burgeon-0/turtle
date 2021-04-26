package org.burgeon.turtle.core.event.source;

import java.util.HashMap;
import java.util.Map;

/**
 * 事件来源
 *
 * @author Sam Lu
 * @createdOn 2021/4/19
 */
public abstract class EventSource {

    private static final Map<String, EventSource> NAME_MAP = new HashMap<>();
    private static final Map<String, EventSource> CLASS_NAME_MAP = new HashMap<>();

    public EventSource() {
        NAME_MAP.put(getName(), this);
        CLASS_NAME_MAP.put(getClass().getSimpleName(), this);
    }

    /**
     * 来源编码
     *
     * @return
     */
    public abstract int getCode();

    /**
     * 来源名称
     *
     * @return
     */
    public abstract String getName();

    /**
     * 是否包含该EventSource
     *
     * @param name
     * @return
     */
    public static boolean contains(String name) {
        EventSource eventSource = NAME_MAP.get(name);
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
        return NAME_MAP.get(name);
    }

    /**
     * 通过Class获取EventSource
     *
     * @param clazz
     * @return
     */
    public static EventSource fromClass(Class clazz) {
        return CLASS_NAME_MAP.get(clazz.getSimpleName());
    }

}
