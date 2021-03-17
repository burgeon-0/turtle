package org.burgeon.turtle.core.process;

import org.burgeon.turtle.core.model.api.ParameterType;

/**
 * 抽象SON转换器
 *
 * @author luxiaocong
 * @createdOn 2021/3/17
 */
public abstract class AbstractJsonConverter implements JsonConverter {

    @Override
    public ParameterType inferParameterType(Class<?> clazz) {
        ParameterType type = null;
        if (clazz.equals(java.lang.Byte.class)) {
            type = ParameterType.NUMBER;
        } else if (clazz.equals(java.lang.Short.class)) {
            type = ParameterType.NUMBER;
        } else if (clazz.equals(java.lang.Integer.class)) {
            type = ParameterType.NUMBER;
        } else if (clazz.equals(java.lang.Long.class)) {
            type = ParameterType.NUMBER;
        } else if (clazz.equals(java.lang.Float.class)) {
            type = ParameterType.NUMBER;
        } else if (clazz.equals(java.lang.Double.class)) {
            type = ParameterType.NUMBER;
        } else if (clazz.equals(java.math.BigInteger.class)) {
            type = ParameterType.NUMBER;
        } else if (clazz.equals(java.math.BigDecimal.class)) {
            type = ParameterType.NUMBER;
        } else if (clazz.equals(java.lang.Boolean.class)) {
            type = ParameterType.BOOLEAN;
        } else if (clazz.equals(java.lang.Character.class)) {
            type = ParameterType.STRING;
        } else if (clazz.equals(java.lang.String.class)) {
            type = ParameterType.STRING;
        } else if (clazz.equals(java.util.ArrayList.class)) {
            type = ParameterType.ARRAY;
        } else if (clazz.equals(java.util.Map.class)) {
            type = ParameterType.OBJECT;
        }
        return type;
    }

}
