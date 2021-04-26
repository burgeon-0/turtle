package org.burgeon.turtle.core.model.api;

import lombok.Data;

/**
 * HTTP头
 *
 * @author Sam Lu
 * @createdOn 2021/2/26
 */
@Data
public class HttpHeader {

    /**
     * Header名称
     */
    private String name;

    /**
     * Header描述
     */
    private String description;

    /**
     * 示例值
     */
    private String exampleValue;

}
