package org.burgeon.turtle.core.model;

import lombok.Data;

import java.util.List;

/**
 * 项目
 *
 * @author luxiaocong
 * @createdOn 2021/2/26
 */
@Data
public class Project {

    /**
     * 项目根路径
     */
    private String host;

    /**
     * HTTP接口
     */
    private List<HttpApi> httpApis;

}
