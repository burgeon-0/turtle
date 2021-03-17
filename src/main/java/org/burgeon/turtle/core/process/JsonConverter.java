package org.burgeon.turtle.core.process;

import org.burgeon.turtle.core.model.api.ParameterType;

/**
 * JSON转换器
 *
 * @author luxiaocong
 * @createdOn 2021/3/16
 */
public interface JsonConverter {

    /**
     * 将对象转换成JSON
     *
     * @param obj
     * @return
     */
    String convert(Object obj);

    /**
     * 将JSON转换成对象
     *
     * @param json
     * @return
     */
    Object convert(String json);

    /**
     * 推断参数类型
     *
     * @param clazz
     * @return
     */
    ParameterType inferParameterType(Class<?> clazz);

}
