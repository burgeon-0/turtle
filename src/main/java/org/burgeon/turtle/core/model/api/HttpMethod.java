package org.burgeon.turtle.core.model.api;

/**
 * HTTP方法
 *
 * @author Sam Lu
 * @createdOn 2021/2/26
 */
public enum HttpMethod {

    /**
     * HTTP GET
     */
    GET,
    /**
     * HTTP POST, Request has body
     */
    POST,
    /**
     * HTTP PUT, Request has body
     */
    PUT,
    /**
     * HTTP DELETE, Request has body
     */
    DELETE,
    /**
     * HTTP HEAD
     */
    HEAD,
    /**
     * HTTP OPTIONS
     */
    OPTIONS,
    /**
     * HTTP TRACE
     */
    TRACE,
    /**
     * HTTP PATCH, Request has body
     */
    PATCH

}
