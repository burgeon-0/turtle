package org.burgeon.turtle.core.model.api;

import lombok.Data;

import java.util.List;

/**
 * HTTP请求
 *
 * @author Sam Lu
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
