package org.burgeon.turtle.core.common;

import org.burgeon.turtle.core.model.api.HttpHeader;

import java.util.List;

/**
 * Http工具
 *
 * @author luxiaocong
 * @createdOn 2021/3/24
 */
public class HttpHelper {

    private static final String CONTENT_TYPE = "Content-Type";

    /**
     * Get HTTP Content-Type
     */
    public static String getContentType(List<HttpHeader> headers) {
        if (headers != null) {
            for (HttpHeader header : headers) {
                if (CONTENT_TYPE.equalsIgnoreCase(header.getName())) {
                    return header.getDescription();
                }
            }
        }
        return null;
    }

}
