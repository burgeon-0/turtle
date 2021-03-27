package org.burgeon.turtle.core.process;

/**
 * 标识参数位置
 *
 * @author luxiaocong
 * @createdOn 2021/3/19
 */
public enum ParameterPosition {

    /**
     * 表示参数为：方法返回参数
     */
    METHOD_RETURN,
    /**
     * 表示参数为：数组子参数，如java.lang.String[]，父参数类型为Array，子参数类型为java.lang.String
     */
    ARRAY_SUB,
    /**
     * 表示参数为：普通参数
     */
    NORMAL

}
