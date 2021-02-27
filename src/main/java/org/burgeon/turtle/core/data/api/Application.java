package org.burgeon.turtle.core.data.api;

import lombok.Data;

import java.util.List;

/**
 * 应用
 *
 * @author luxiaocong
 * @createdOn 2021/2/26
 */
@Data
public class Application {

    /**
     * 应用名称
     */
    private String name;

    /**
     * 应用描述
     */
    private String description;

    /**
     * 应用根路径
     */
    private String host;

    /**
     * HTTP接口
     */
    private List<HttpApi> httpApis;

}
