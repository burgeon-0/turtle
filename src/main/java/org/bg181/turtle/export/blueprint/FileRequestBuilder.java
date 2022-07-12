package org.bg181.turtle.export.blueprint;

import org.bg181.turtle.core.common.Constants;
import org.bg181.turtle.core.model.api.HttpHeader;
import org.bg181.turtle.core.model.api.HttpRequest;
import org.bg181.turtle.core.model.api.Parameter;
import org.bg181.turtle.core.model.api.ParameterType;

import java.util.List;

import static org.bg181.turtle.export.blueprint.Constants.*;

/**
 * 文件请求构建器
 *
 * @author Sam Lu
 * @createdOn 2021/4/12
 */
public class FileRequestBuilder {

    private static final String BOUNDARY = "----ABCBABCBA";
    private static final String MULTIPART_FORM_DATA = "multipart/form-data; boundary=" + BOUNDARY;

    private StringBuilder builder = new StringBuilder();

    /**
     * 构建文件请求
     *
     * @param httpRequest
     * @return
     */
    public FileRequestBuilder buildRequest(HttpRequest httpRequest) {
        builder.append(LINE_BREAK);
        builder.append(REQUEST).append(SPACE).append(LEFT_PARENTHESES);
        builder.append(MULTIPART_FORM_DATA).append(RIGHT_PARENTHESES).append(LINE_BREAK);
        if (httpRequest.getHeaders() != null && !httpRequest.getHeaders().isEmpty()) {
            buildHeaders(httpRequest.getHeaders());
        }
        buildParameters(httpRequest.getBody());
        return this;
    }

    /**
     * 构建Http请求和返回头，Content-Type在Request或Response标签中已有定义，在此处需将其过滤掉
     *
     * @param headers
     */
    private void buildHeaders(List<HttpHeader> headers) {
        if (headers.size() == 1 && Constants.CONTENT_TYPE.equals(headers.get(0).getName())) {
            return;
        }
        builder.append(TAB).append(HEADERS).append(LINE_BREAK).append(LINE_BREAK);
        for (HttpHeader header : headers) {
            if (org.bg181.turtle.core.common.Constants.CONTENT_TYPE.equals(header.getName())) {
                continue;
            }
            builder.append(TAB).append(TAB).append(TAB).append(header.getName()).append(COLON);
            builder.append(SPACE).append(header.getDescription()).append(LINE_BREAK);
        }
    }

    /**
     * 构建multipart/form-data参数
     *
     * @param parameters
     */
    private void buildParameters(List<Parameter> parameters) {
        if (parameters != null && !parameters.isEmpty()) {
            builder.append(LINE_BREAK);
            builder.append(TAB).append(BODY).append(LINE_BREAK).append(LINE_BREAK);
            for (Parameter parameter : parameters) {
                buildParameter(parameter, null);
            }
            builder.append(TAB).append(TAB).append(TAB).append(BOUNDARY).append(LINE_BREAK);
        }
    }

    /**
     * 构建multipart/form-data参数
     *
     * @param parameter
     * @param suffix
     */
    private void buildParameter(Parameter parameter, String suffix) {
        String parameterName = parameter.getName();
        if (parameter.getParentParameter() != null
                && parameter.getParentParameter().getType() == ParameterType.ARRAY) {
            parameterName = "";
        }
        if (suffix != null) {
            if ("".equals(parameterName)) {
                parameterName = suffix;
            } else {
                parameterName = String.format("%s.%s", suffix, parameterName);
            }
        }
        if (parameter.getType() == ParameterType.NUMBER
                || parameter.getType() == ParameterType.STRING
                || parameter.getType() == ParameterType.BOOLEAN) {
            builder.append(TAB).append(TAB).append(TAB).append(BOUNDARY).append(LINE_BREAK);
            builder.append(TAB).append(TAB).append(TAB).append(String.format
                    ("Content-Disposition: form-data; name=\"%s\"", parameterName));
            builder.append(LINE_BREAK).append(LINE_BREAK);
            builder.append(TAB).append(TAB).append(TAB);
            if (parameter.getParentParameter() != null
                    && parameter.getParentParameter().getType() == ParameterType.ARRAY) {
                builder.append("{array}");
            } else {
                builder.append(String.format("{%s}", parameter.getType().toString().toLowerCase()));
            }
            builder.append(LINE_BREAK);
        } else if (parameter.getType() == ParameterType.FILE) {
            builder.append(TAB).append(TAB).append(TAB).append(BOUNDARY).append(LINE_BREAK);
            builder.append(TAB).append(TAB).append(TAB).append(String.format
                    ("Content-Disposition: form-data; name=\"%s\"; filename=\"{filename}\"", parameterName));
            builder.append(LINE_BREAK).append(LINE_BREAK);
            builder.append(TAB).append(TAB).append(TAB).append("{data}").append(LINE_BREAK);
        } else {
            if (parameter.getParentParameter() != null) {
                suffix = parameterName;
            }
            List<Parameter> childParameters = parameter.getChildParameters();
            if (parameter.getChildParameters() != null) {
                for (Parameter childParameter : childParameters) {
                    buildParameter(childParameter, suffix);
                }
            }
        }
    }

    /**
     * 完成构建
     *
     * @return
     */
    public String build() {
        return builder.toString();
    }

    /**
     * 清空构建历史
     *
     * @return
     */
    public void reset() {
        builder.delete(0, builder.length());
    }

}
