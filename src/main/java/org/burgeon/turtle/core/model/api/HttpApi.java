package org.burgeon.turtle.core.model.api;

import lombok.Data;

import java.util.List;

/**
 * HTTP接口
 *
 * @author luxiaocong
 * @createdOn 2021/2/26
 */
@Data
public class HttpApi {

    /**
     * 以类的全限定名+方法签名作为ID
     */
    private String id;

    /**
     * 接口名称
     */
    private String name;

    /**
     * 接口描述
     */
    private String description;

    /**
     * 接口版本
     */
    private String version;

    /**
     * HTTP方法
     */
    private HttpMethod httpMethod;

    /**
     * 接口路径
     */
    private String path;

    /**
     * PATH参数
     */
    private List<Parameter> pathParameters;

    /**
     * URI参数
     */
    private List<Parameter> uriParameters;

    /**
     * HTTP请求
     */
    private HttpRequest httpRequest;

    /**
     * HTTP返回
     */
    private HttpResponse httpResponse;

    /**
     * 接口错误码
     */
    private List<ErrorCode> errorCodes;

}
