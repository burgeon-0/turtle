package org.burgeon.turtle.core.model.source;

import lombok.Data;

/**
 * Java类型
 *
 * @author luxiaocong
 * @createdOn 2021/2/27
 */
@Data
public class JavaType {

    private boolean isByte;

    private boolean isShort;

    private boolean isInt;

    private boolean isLong;

    private boolean isFloat;

    private boolean isDouble;

    private boolean isBoolean;

    private boolean isChar;

    private boolean isVoid;

    /**
     * Java基础类型：byte、short、int、long、float、double、boolean、char
     */
    private boolean isPrimitive;

    /**
     * 类型为Object，则可以转换成JavaClass
     */
    private boolean isObject;

    private boolean isArray;

    /**
     * 数组维度
     */
    private int arrayDimension;

    private boolean isSet;

    private boolean isList;

    private boolean isMap;

}
