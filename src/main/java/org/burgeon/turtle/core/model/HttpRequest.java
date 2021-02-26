package org.burgeon.turtle.core.model;

import lombok.Data;

import java.util.List;

/**
 * HTTP请求
 *
 * @author luxiaocong
 * @createdOn 2021/2/26
 */
@Data
public class HttpRequest {

    /**
     * HTTP请求头
     */
    private List<HttpHeader> headers;

    /**
     * HTTP请求body
     */
    private List<Parameter> body;

}
