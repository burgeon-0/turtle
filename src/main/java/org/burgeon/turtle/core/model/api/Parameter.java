package org.burgeon.turtle.core.model.api;

import lombok.Data;

import java.util.List;

/**
 * 参数
 *
 * @author luxiaocong
 * @createdOn 2021/2/26
 */
@Data
public class Parameter {

    /**
     * 参数类型
     */
    private ParameterType type;

    /**
     * 参数名称
     */
    private String name;

    /**
     * 是否是必须的
     */
    private boolean isRequired;

    /**
     * 参数描述
     */
    private String description;

    /**
     * 示例值
     */
    private String exampleValue;

    /**
     * 子参数，只有Object类型的参数才有子参数
     *
     * @see ParameterType
     */
    private List<Parameter> childParameters;

}
