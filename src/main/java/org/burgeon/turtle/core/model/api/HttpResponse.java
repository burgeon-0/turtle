package org.burgeon.turtle.core.model.api;

import lombok.Data;

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
    private HttpHeader[] headers;

    /**
     * HTTP返回body
     */
    private Parameter[] body;

    /**
     * HTTP状态码
     */
    private int status;

}
