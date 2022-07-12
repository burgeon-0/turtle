package org.bg181.turtle.export.blueprint.model;

import org.bg181.turtle.core.model.api.HttpHeader;
import org.bg181.turtle.export.blueprint.FilterHelper;

/**
 * HTTP头【代理】
 *
 * @author Sam Lu
 * @createdOn 2021/3/27
 */
public class HttpHeaderProxy extends HttpHeader {

    private HttpHeader httpHeader;

    public HttpHeaderProxy(HttpHeader httpHeader) {
        this.httpHeader = httpHeader;
    }

    @Override
    public String getName() {
        return FilterHelper.filterHttpHeaderName(httpHeader.getName());
    }

    @Override
    public String getDescription() {
        return FilterHelper.filterHttpHeaderDescription(httpHeader.getDescription());
    }

    @Override
    public String getExampleValue() {
        return httpHeader.getExampleValue();
    }

}
