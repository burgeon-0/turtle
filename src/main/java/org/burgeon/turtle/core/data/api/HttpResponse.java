package org.burgeon.turtle.core.data.api;

import lombok.Data;

import java.util.List;

/**
 * HTTP返回
 *
 * @author luxiaocong
 * @createdOn 2021/2/26
 */
@Data
public class HttpResponse {

    /**
     * HTTP返回头
     */
    private List<HttpHeader> headers;

    /**
     * HTTP返回body
     */
    private List<Parameter> body;

    /**
     * HTTP状态码
     */
    private int status;

}
