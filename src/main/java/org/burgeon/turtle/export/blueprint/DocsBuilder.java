package org.burgeon.turtle.export.blueprint;

import org.burgeon.turtle.core.common.Constants;
import org.burgeon.turtle.core.common.HttpHelper;
import org.burgeon.turtle.core.model.api.*;
import org.burgeon.turtle.core.utils.StringUtils;

import java.util.List;

/**
 * 文档构建器
 *
 * @author luxiaocong
 * @createdOn 2021/3/24
 */
public class DocsBuilder {

    private static final String VERSION_HEADER = "FORMAT: 1A";
    private static final String HOST_PREFIX = "HOST: ";

    private static final String LINE_BREAK = Constants.SEPARATOR_LINE_BREAK;
    private static final String SPACE = Constants.SEPARATOR_SPACE;
    private static final String COLON = Constants.SEPARATOR_COLON;
    private static final String COMMA = Constants.SEPARATOR_COMMA;
    private static final String PLUS = "+";
    private static final String MINUS = "-";
    private static final String EQUAL = "=";
    private static final String TAB = "\t";
    private static final String HASH_MARK = "#";
    private static final String QUESTION_MARK = "?";
    private static final String AND_MARK = "&";
    private static final String LEFT_BRACE = "{";
    private static final String RIGHT_BRACE = "}";
    private static final String LEFT_BRACKET = "[";
    private static final String RIGHT_BRACKET = "]";
    private static final String LEFT_PARENTHESES = "(";
    private static final String RIGHT_PARENTHESES = ")";

    private static final String GROUP = "Group";
    private static final String PARAMETERS = "+ Parameters";
    private static final String REQUEST = "+ Request";
    private static final String RESPONSE = "+ Response";
    private static final String HEADERS = "+ Headers";
    private static final String ATTRIBUTES = "+ Attributes";
    private static final String REQUIRED = "required";
    private static final String OPTIONAL = "optional";
    private static final String FIXED_TYPE = "fixed-type";
    private static final String OBJECT = "(object)";
    private static final String DEFAULT_CONTENT_TYPE = "application/json";

    private StringBuilder builder = new StringBuilder();

    public DocsBuilder() {
        builder.append(VERSION_HEADER).append(LINE_BREAK);
    }

    /**
     * 添加API Host
     *
     * @param host
     * @return
     */
    public DocsBuilder appendHost(String host) {
        if (StringUtils.notBlank(host)) {
            builder.append(HOST_PREFIX).append(host).append(LINE_BREAK);
        }
        return this;
    }

    /**
     * 添加文档标题
     *
     * @param title
     * @return
     */
    public DocsBuilder appendTitle(String title) {
        if (StringUtils.notBlank(title)) {
            builder.append(LINE_BREAK);
            builder.append(HASH_MARK).append(SPACE).append(title).append(LINE_BREAK);
        } else {
            builder.append(LINE_BREAK);
            builder.append(HASH_MARK).append(LINE_BREAK);
        }
        return this;
    }

    /**
     * 添加文档描述
     *
     * @param description
     * @return
     */
    public DocsBuilder appendDescription(String description) {
        if (StringUtils.notBlank(description)) {
            builder.append(LINE_BREAK);
            builder.append(description).append(LINE_BREAK);
        }
        return this;
    }

    /**
     * 添加API群组标题
     *
     * @param name
     * @param version
     * @return
     */
    public DocsBuilder appendGroupTitle(String name, String version) {
        if (StringUtils.notBlank(name)) {
            builder.append(LINE_BREAK);
            builder.append(HASH_MARK).append(HASH_MARK).append(SPACE);
            builder.append(GROUP).append(SPACE).append(name);
            if (StringUtils.notBlank(version)) {
                builder.append(SPACE).append(version);
            }
            builder.append(LINE_BREAK);
        }
        return this;
    }

    /**
     * 添加API群组描述
     *
     * @param description
     * @return
     */
    public DocsBuilder appendGroupDescription(String description) {
        if (StringUtils.notBlank(description)) {
            builder.append(LINE_BREAK);
            builder.append(description).append(LINE_BREAK);
        }
        return this;
    }

    /**
     * 添加API
     *
     * @param name
     * @param version
     * @param httpMethod
     * @param path
     * @param uriParameters
     * @return
     */
    public DocsBuilder appendApi(String name, String version, HttpMethod httpMethod, String path,
                                 List<Parameter> uriParameters) {
        builder.append(LINE_BREAK);
        builder.append(HASH_MARK).append(HASH_MARK).append(HASH_MARK).append(SPACE);
        builder.append(name);
        if (StringUtils.notBlank(version)) {
            builder.append(SPACE).append(version);
        }
        builder.append(SPACE).append(LEFT_BRACKET).append(httpMethod);
        builder.append(SPACE).append(path);
        if (uriParameters != null && uriParameters.size() > 0) {
            builder.append(QUESTION_MARK);
            for (int i = 0; i < uriParameters.size(); i++) {
                Parameter parameter = uriParameters.get(i);
                builder.append(parameter.getName()).append(EQUAL);
                builder.append(LEFT_BRACE).append(parameter.getName()).append(RIGHT_BRACE);
                if (i < uriParameters.size() - 1) {
                    builder.append(AND_MARK);
                }
            }
        }
        builder.append(RIGHT_BRACKET).append(LINE_BREAK);
        return this;
    }

    /**
     * 添加API描述
     *
     * @param description
     * @return
     */
    public DocsBuilder appendApiDescription(String description) {
        if (StringUtils.notBlank(description)) {
            builder.append(LINE_BREAK);
            builder.append(description).append(LINE_BREAK);
        }
        return this;
    }

    /**
     * 添加Http Path参数
     *
     * @param parameters
     * @return
     */
    public DocsBuilder appendPathParameters(List<Parameter> parameters) {
        buildParameters(parameters);
        return this;
    }

    /**
     * 添加Http Uri参数
     *
     * @param parameters
     * @return
     */
    public DocsBuilder appendUriParameters(List<Parameter> parameters) {
        buildParameters(parameters);
        return this;
    }

    /**
     * 添加HttpRequest
     *
     * @param httpRequest
     * @return
     */
    public DocsBuilder appendHttpRequest(HttpRequest httpRequest) {
        if (httpRequest != null) {
            builder.append(LINE_BREAK);
            builder.append(REQUEST).append(SPACE);
            buildContentType(httpRequest.getHeaders());
            builder.append(LINE_BREAK);
            if (httpRequest.getHeaders() != null && httpRequest.getHeaders().size() > 0) {
                buildHeaders(httpRequest.getHeaders());
            }
            if (httpRequest.getBody() != null && httpRequest.getBody().size() > 0) {
                buildAttributes(httpRequest.getBody());
            }
        }
        return this;
    }

    /**
     * 添加HttpResponse
     *
     * @param httpResponse
     * @return
     */
    public DocsBuilder appendHttpResponse(HttpResponse httpResponse) {
        if (httpResponse != null) {
            builder.append(LINE_BREAK);
            builder.append(RESPONSE).append(SPACE).append(httpResponse.getStatus()).append(SPACE);
            buildContentType(httpResponse.getHeaders());
            builder.append(LINE_BREAK);
            if (httpResponse.getHeaders() != null && httpResponse.getHeaders().size() > 0) {
                buildHeaders(httpResponse.getHeaders());
            }
            if (httpResponse.getBody() != null && httpResponse.getBody().size() > 0) {
                Parameter returnParameter = httpResponse.getBody().get(0);
                if (returnParameter.getType() == ParameterType.OBJECT) {
                    buildAttributes(returnParameter.getChildParameters());
                }
            }
        }
        return this;
    }

    /**
     * 添加错误码
     *
     * @param errorCodes
     * @return
     */
    public DocsBuilder appendErrorCodes(List<ErrorCode> errorCodes) {
        return this;
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
     * 构建Http Path和Uri参数
     *
     * @param parameters
     */
    private void buildParameters(List<Parameter> parameters) {
        if (parameters != null && !parameters.isEmpty()) {
            builder.append(LINE_BREAK);
            builder.append(PARAMETERS).append(LINE_BREAK);
            for (Parameter parameter : parameters) {
                buildParameter(parameter, 1);
            }
        }
    }

    /**
     * 构建Http请求和返回ContentType
     *
     * @param headers
     */
    private void buildContentType(List<HttpHeader> headers) {
        if (HttpHelper.getContentType(headers) != null) {
            builder.append(LEFT_PARENTHESES);
            builder.append(HttpHelper.getContentType(headers));
            builder.append(RIGHT_PARENTHESES);
        } else {
            builder.append(LEFT_PARENTHESES);
            builder.append(DEFAULT_CONTENT_TYPE);
            builder.append(RIGHT_PARENTHESES);
        }
    }

    /**
     * 构建Http请求和返回头
     *
     * @param headers
     */
    private void buildHeaders(List<HttpHeader> headers) {
        builder.append(TAB).append(HEADERS).append(LINE_BREAK).append(LINE_BREAK);
        for (HttpHeader header : headers) {
            builder.append(TAB).append(TAB).append(TAB).append(header.getName()).append(COLON);
            builder.append(SPACE).append(header.getDescription()).append(LINE_BREAK);
        }
    }

    /**
     * 构建Http请求和返回参数
     *
     * @param parameters
     */
    private void buildAttributes(List<Parameter> parameters) {
        if (parameters != null && !parameters.isEmpty()) {
            builder.append(TAB).append(ATTRIBUTES).append(LINE_BREAK);
            for (Parameter parameter : parameters) {
                if (parameter.getParentParameter() == null
                        && parameter.getType() == ParameterType.OBJECT) {
                    List<Parameter> childParameters = parameter.getChildParameters();
                    for (Parameter childParameter : childParameters) {
                        buildParameter(childParameter, 2);
                    }
                } else {
                    buildParameter(parameter, 2);
                }
            }
        }
    }

    /**
     * 构建Http参数
     *
     * @param parameter
     * @param indent
     */
    private void buildParameter(Parameter parameter, int indent) {
        if (parameter.getParentParameter() != null
                && parameter.getParentParameter().getType() == ParameterType.ARRAY) {
            if (parameter.getType() == ParameterType.OBJECT) {
                indent++;
                for (int i = 0; i < indent; i++) {
                    builder.append(TAB);
                }
                builder.append(PLUS).append(SPACE).append(OBJECT).append(LINE_BREAK);
            }
        } else {
            for (int i = 0; i < indent; i++) {
                builder.append(TAB);
            }
            builder.append(PLUS).append(SPACE).append(parameter.getName());
            builder.append(SPACE).append(LEFT_PARENTHESES);
            if (parameter.isRequired()) {
                builder.append(REQUIRED);
            } else {
                builder.append(OPTIONAL);
            }
            builder.append(COMMA).append(SPACE);
            builder.append(parameter.getType().toString().toLowerCase());
            if (parameter.getType() == ParameterType.ARRAY
                    && parameter.getChildParameters().get(0).getType() == ParameterType.OBJECT) {
                builder.append(COMMA).append(SPACE).append(FIXED_TYPE);
            }
            builder.append(RIGHT_PARENTHESES);
            if (StringUtils.notBlank(parameter.getDescription())) {
                String description = parameter.getDescription();
                builder.append(SPACE).append(MINUS).append(SPACE).append(description);
            }
            builder.append(LINE_BREAK);
        }

        buildChildParameters(parameter, indent);
    }

    /**
     * 构建Http子参数
     *
     * @param parameter
     * @param indent
     */
    private void buildChildParameters(Parameter parameter, int indent) {
        if (parameter.getType() == ParameterType.ARRAY
                || parameter.getType() == ParameterType.OBJECT) {
            List<Parameter> childParameters = parameter.getChildParameters();
            if (childParameters != null && childParameters.size() > 0) {
                if (parameter.getType() != ParameterType.ARRAY) {
                    indent++;
                }
                for (Parameter childParameter : childParameters) {
                    buildParameter(childParameter, indent);
                }
            }
        }
    }

}
