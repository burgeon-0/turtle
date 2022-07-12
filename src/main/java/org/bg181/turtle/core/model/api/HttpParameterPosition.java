package org.bg181.turtle.core.model.api;

/**
 * 标识HTTP参数位置
 *
 * @author Sam Lu
 * @createdOn 2021/3/19
 */
public enum HttpParameterPosition {

    /**
     * 表示参数为：Path参数
     */
    PATH,
    /**
     * 表示参数为：URI参数
     */
    URI,
    /**
     * 表示参数为：Request参数
     */
    REQUEST,
    /**
     * 表示参数为：Response参数
     */
    RESPONSE

}
