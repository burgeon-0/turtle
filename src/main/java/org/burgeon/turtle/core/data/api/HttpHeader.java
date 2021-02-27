package org.burgeon.turtle.core.data.api;

import lombok.Data;

/**
 * HTTP头
 *
 * @author luxiaocong
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
