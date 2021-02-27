package org.burgeon.turtle.core.model.api;

import lombok.Data;

import java.util.List;

/**
 * API项目
 *
 * @author luxiaocong
 * @createdOn 2021/2/26
 */
@Data
public class ApiProject {

    /**
     * 项目名称
     */
    private String name;

    /**
     * 项目描述
     */
    private String description;

    /**
     * 项目根路径
     */
    private String host;

    /**
     * HTTP接口
     */
    private List<HttpApi> httpApis;

}
