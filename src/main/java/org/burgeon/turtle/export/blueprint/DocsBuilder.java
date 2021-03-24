package org.burgeon.turtle.export.blueprint;

import org.burgeon.turtle.core.common.Constants;
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
    private static final String HASHTAG = "#";
    private static final String TAB = "\t";
    private static final String PLUS = "+";
    private static final String MINUS = "-";
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

    private static final String DATA_STRUCTURES = "# Data Structures";

    private StringBuilder builder = new StringBuilder();

    public DocsBuilder() {
        builder.append(VERSION_HEADER).append(LINE_BREAK);
    }

    public DocsBuilder appendHost(String host) {
        if (StringUtils.notBlank(host)) {
            builder.append(HOST_PREFIX).append(host).append(LINE_BREAK);
        }
        return this;
    }

    public DocsBuilder appendTitle(String title) {
        if (StringUtils.notBlank(title)) {
            builder.append(LINE_BREAK);
            builder.append(HASHTAG).append(SPACE).append(title).append(LINE_BREAK);
        } else {
            builder.append(LINE_BREAK);
            builder.append(HASHTAG).append(LINE_BREAK);
        }
        return this;
    }

    public DocsBuilder appendDescription(String description) {
        if (StringUtils.notBlank(description)) {
            builder.append(LINE_BREAK);
            builder.append(description).append(LINE_BREAK);
        }
        return this;
    }

    public DocsBuilder appendGroupTitle(String name, String version) {
        if (StringUtils.notBlank(name)) {
            builder.append(LINE_BREAK);
            builder.append(HASHTAG).append(HASHTAG).append(SPACE);
            builder.append(GROUP).append(SPACE).append(name);
            if (StringUtils.notBlank(version)) {
                builder.append(SPACE).append(version);
            }
            builder.append(LINE_BREAK);
        }
        return this;
    }

    public DocsBuilder appendGroupDescription(String description) {
        if (StringUtils.notBlank(description)) {
            builder.append(LINE_BREAK);
            builder.append(description).append(LINE_BREAK);
        }
        return this;
    }

    public DocsBuilder appendApi(String name, String version, HttpMethod httpMethod, String path) {
        builder.append(LINE_BREAK);
        builder.append(HASHTAG).append(HASHTAG).append(HASHTAG).append(SPACE);
        builder.append(name);
        if (StringUtils.notBlank(version)) {
            builder.append(SPACE).append(version);
        }
        builder.append(SPACE).append(LEFT_BRACKET).append(httpMethod);
        builder.append(SPACE).append(path).append(RIGHT_BRACKET).append(LINE_BREAK);
        return this;
    }

    public DocsBuilder appendApiDescription(String description) {
        if (StringUtils.notBlank(description)) {
            builder.append(LINE_BREAK);
            builder.append(description).append(LINE_BREAK);
        }
        return this;
    }

    public DocsBuilder appendPathParameters(List<Parameter> parameters) {
        buildParameters(parameters);
        return this;
    }

    public DocsBuilder appendUriParameters(List<Parameter> parameters) {
        buildParameters(parameters);
        return this;
    }

    public DocsBuilder appendHttpRequest(HttpRequest httpRequest) {
        if (httpRequest != null) {
            builder.append(LINE_BREAK);
            builder.append(REQUEST).append(SPACE).append("(application/json)").append(LINE_BREAK);
            if (httpRequest.getHeaders() != null && httpRequest.getHeaders().size() > 0) {
                buildHeaders(httpRequest.getHeaders());
            }
            if (httpRequest.getBody() != null && httpRequest.getBody().size() > 0) {
                buildAttributes(httpRequest.getBody());
            }
        }
        return this;
    }

    public DocsBuilder appendHttpResponse(HttpResponse httpResponse) {
        if (httpResponse != null) {
            builder.append(LINE_BREAK);
            builder.append(RESPONSE).append(SPACE).append(httpResponse.getStatus()).append(SPACE);
            builder.append("(application/json)").append(LINE_BREAK);
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

    public DocsBuilder appendErrorCodes(List<ErrorCode> errorCodes) {
        return this;
    }

    public String build() {
        return builder.toString();
    }

    private void buildParameters(List<Parameter> parameters) {
        if (parameters != null && !parameters.isEmpty()) {
            builder.append(LINE_BREAK);
            builder.append(PARAMETERS).append(LINE_BREAK);
            for (Parameter parameter : parameters) {
                buildParameter(parameter, 1);
            }
        }
    }

    private void buildHeaders(List<HttpHeader> headers) {
        builder.append(TAB).append(HEADERS).append(LINE_BREAK);
        for (HttpHeader header : headers) {
            builder.append(TAB).append(TAB).append(header.getName()).append(COLON);
            builder.append(SPACE).append(header.getDescription()).append(LINE_BREAK);
        }
    }

    private void buildAttributes(List<Parameter> parameters) {
        if (parameters != null && !parameters.isEmpty()) {
            builder.append(TAB).append(ATTRIBUTES).append(LINE_BREAK);
            for (Parameter parameter : parameters) {
                buildParameter(parameter, 2);
            }
        }
    }

    private void buildParameter(Parameter parameter, int indent) {
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
        builder.append(parameter.getType().toString().toLowerCase()).append(RIGHT_PARENTHESES);
        if (StringUtils.notBlank(parameter.getDescription())) {
            String description = parameter.getDescription();
            description = description.replaceAll("-", "");
            builder.append(SPACE).append(MINUS).append(SPACE).append(description);
        }
        builder.append(LINE_BREAK);

        if (parameter.getType() == ParameterType.OBJECT) {
            List<Parameter> childParameters = parameter.getChildParameters();
            if (childParameters != null && childParameters.size() > 0) {
                indent++;
                for (Parameter childParameter : childParameters) {
                    buildParameter(childParameter, indent);
                }
            }
        }
    }

}
