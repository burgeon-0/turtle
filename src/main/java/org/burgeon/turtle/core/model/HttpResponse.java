package org.burgeon.turtle.core.model;

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
     * HTTP状态码
     */
    private int status;

    /**
     * HTTP返回body
     */
    private List<Parameter> body;

}
