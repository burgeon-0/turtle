package org.bg181.turtle.core.event.target;

import java.util.HashMap;
import java.util.Map;

/**
 * 事件目标
 *
 * @author Sam Lu
 * @createdOn 2021/4/19
 */
public abstract class EventTarget {

    private static final Map<String, EventTarget> NAME_MAP = new HashMap<>();
    private static final Map<String, EventTarget> CLASS_NAME_MAP = new HashMap<>();

    public EventTarget() {
        NAME_MAP.put(this.getName(), this);
        CLASS_NAME_MAP.put(getClass().getSimpleName(), this);
    }

    /**
     * 目标编码
     *
     * @return
     */
    public abstract int getCode();

    /**
     * 目标名称
     *
     * @return
     */
    public abstract String getName();

    /**
     * 是否包含该EventTarget
     *
     * @param name
     * @return
     */
    public static boolean contains(String name) {
        EventTarget eventTarget = NAME_MAP.get(name);
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
        return NAME_MAP.get(name);
    }

    /**
     * 通过Class获取EventTarget
     *
     * @param clazz
     * @return
     */
    public static EventTarget fromClass(Class clazz) {
        return CLASS_NAME_MAP.get(clazz.getSimpleName());
    }

}
