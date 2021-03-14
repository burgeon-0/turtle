package org.burgeon.turtle.core.model.api;

import lombok.Data;

import java.util.List;

/**
 * API群组，属于同一个类的接口则属于同一个群组
 *
 * @author luxiaocong
 * @createdOn 2021/3/14
 */
@Data
public class ApiGroup {

    /**
     * 以类的全限定名作为ID
     */
    private String id;

    /**
     * 群组名称
     */
    private String name;

    /**
     * 群组描述
     */
    private String description;

    /**
     * 群组的接口版本
     */
    private String version;

    /**
     * HTTP接口
     */
    private List<HttpApi> httpApis;

}
