package org.bg181.turtle.core.common;

import org.bg181.turtle.core.model.api.HttpHeader;

import java.util.List;

/**
 * Http工具
 *
 * @author Sam Lu
 * @createdOn 2021/3/24
 */
public class HttpHelper {

    /**
     * Get HTTP Content-Type
     */
    public static String getContentType(List<HttpHeader> headers) {
        if (headers != null) {
            for (HttpHeader header : headers) {
                if (Constants.CONTENT_TYPE.equalsIgnoreCase(header.getName())) {
                    return header.getDescription();
                }
            }
        }
        return null;
    }

}
