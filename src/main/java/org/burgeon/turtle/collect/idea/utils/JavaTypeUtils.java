package org.burgeon.turtle.collect.idea.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Java类型判断工具
 *
 * @author luxiaocong
 * @createdOn 2021/3/3
 */
public class JavaTypeUtils {

    private static final String ARRAY_SUFFIX = "[]";

    /**
     * 是否是byte类型
     *
     * @param text
     * @return
     */
    public static boolean isByte(String text) {
        Type type = Type.fromText(text);
        if (type.code == Type.BYTE_0.code) {
            return true;
        }
        return false;
    }

    /**
     * 是否是short类型
     *
     * @param text
     * @return
     */
    public static boolean isShort(String text) {
        Type type = Type.fromText(text);
        if (type.code == Type.SHORT_0.code) {
            return true;
        }
        return false;
    }

    /**
     * 是否是int类型
     *
     * @param text
     * @return
     */
    public static boolean isInt(String text) {
        Type type = Type.fromText(text);
        if (type.code == Type.INT_0.code) {
            return true;
        }
        return false;
    }

    /**
     * 是否是long类型
     *
     * @param text
     * @return
     */
    public static boolean isLong(String text) {
        Type type = Type.fromText(text);
        if (type.code == Type.LONG_0.code) {
            return true;
        }
        return false;
    }

    /**
     * 是否是float类型
     *
     * @param text
     * @return
     */
    public static boolean isFloat(String text) {
        Type type = Type.fromText(text);
        if (type.code == Type.FLOAT_0.code) {
            return true;
        }
        return false;
    }

    /**
     * 是否是double类型
     *
     * @param text
     * @return
     */
    public static boolean isDouble(String text) {
        Type type = Type.fromText(text);
        if (type.code == Type.DOUBLE_0.code) {
            return true;
        }
        return false;
    }

    /**
     * 是否是boolean类型
     *
     * @param text
     * @return
     */
    public static boolean isBoolean(String text) {
        Type type = Type.fromText(text);
        if (type.code == Type.BOOLEAN_0.code) {
            return true;
        }
        return false;
    }

    /**
     * 是否是char类型
     *
     * @param text
     * @return
     */
    public static boolean isChar(String text) {
        Type type = Type.fromText(text);
        if (type.code == Type.CHAR_0.code) {
            return true;
        }
        return false;
    }

    /**
     * 是否是Java基础类型
     *
     * @param text
     * @return
     */
    public static boolean isPrimitive(String text) {
        Type type = Type.fromText(text);
        return type.isPrimitive;
    }

    /**
     * 是否是void类型
     *
     * @param text
     * @return
     */
    public static boolean isVoid(String text) {
        Type type = Type.fromText(text);
        if (type.code == Type.VOID.code) {
            return true;
        }
        return false;
    }

    /**
     * 是否是数组
     *
     * @param text
     * @return
     */
    public static boolean isArray(String text) {
        if (text.contains(ARRAY_SUFFIX)) {
            return true;
        }
        return false;
    }

    /**
     * 获取数组维度
     *
     * @param text
     * @return
     */
    public static int getArrayDimension(String text) {
        int arrayDimension = 0;
        while (text.contains(ARRAY_SUFFIX)) {
            text = text.replace(ARRAY_SUFFIX, "");
            arrayDimension++;
        }
        return arrayDimension;
    }

    private enum Type {
        /**
         * 未知类型
         */
        UNKNOWN("unknown", false, -1),
        /**
         * byte类型
         */
        BYTE_0("byte", true, 1),
        /**
         * Byte类型
         */
        BYTE_1("java.lang.Byte", false, 1),
        /**
         * short类型
         */
        SHORT_0("short", true, 2),
        /**
         * Short类型
         */
        SHORT_1("java.lang.Short", false, 2),
        /**
         * int类型
         */
        INT_0("int", true, 3),
        /**
         * Integer类型
         */
        INT_1("java.lang.Integer", false, 3),
        /**
         * long类型
         */
        LONG_0("long", true, 4),
        /**
         * Long类型
         */
        LONG_1("java.lang.Long", false, 4),
        /**
         * float类型
         */
        FLOAT_0("float", true, 5),
        /**
         * Float类型
         */
        FLOAT_1("java.lang.Float", false, 5),
        /**
         * double类型
         */
        DOUBLE_0("double", true, 6),
        /**
         * Double类型
         */
        DOUBLE_1("java.lang.Double", false, 6),
        /**
         * boolean类型
         */
        BOOLEAN_0("boolean", true, 7),
        /**
         * Boolean类型
         */
        BOOLEAN_1("java.lang.Boolean", false, 7),
        /**
         * char类型
         */
        CHAR_0("char", true, 8),
        /**
         * Charter类型
         */
        CHAR_1("java.lang.Character", false, 8),
        /**
         * void类型
         */
        VOID("void", false, 9);

        private static Map<String, Type> valuesMap = new HashMap<>();

        static {
            for (Type type : Type.values()) {
                valuesMap.put(type.text, type);
            }
        }

        private String text;
        /**
         * 是否是Java基础类型
         */
        private boolean isPrimitive;
        private int code;

        Type(String text, boolean isPrimitive, int code) {
            this.text = text;
            this.isPrimitive = isPrimitive;
            this.code = code;
        }

        /**
         * 从text得到类型
         *
         * @param text
         * @return
         */
        public static Type fromText(String text) {
            Type type = valuesMap.get(text);
            if (type != null) {
                return type;
            }
            return UNKNOWN;
        }
    }

}
