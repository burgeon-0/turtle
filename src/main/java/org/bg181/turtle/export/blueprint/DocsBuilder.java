package org.bg181.turtle.export.blueprint;

import org.bg181.turtle.core.common.Constants;
import org.bg181.turtle.core.common.FileHelper;
import org.bg181.turtle.core.common.HttpHelper;
import org.bg181.turtle.core.model.api.*;
import org.bg181.turtle.core.utils.StringUtils;

import java.util.*;

import static org.bg181.turtle.export.blueprint.Constants.*;

/**
 * 文档构建器
 * TODO 使用阿尔萨斯分析性能消耗
 *
 * @author Sam Lu
 * @createdOn 2021/3/24
 */
public class DocsBuilder {

    private StringBuilder builder = new StringBuilder();

    private ParametersBuilder parametersBuilder = new ParametersBuilder();

    private AttributesBuilder attributesBuilder = new AttributesBuilder();

    private FileRequestBuilder fileRequestBuilder = new FileRequestBuilder();

    private ApiProject apiProject;

    /**
     * API Blueprint不允许出现名字重复的Group，因此，需要：
     * 缓存API群组标题，出现重复时进行重命名，在标题后加上后缀-1、-2、-3...
     */
    private Map<String, Integer> groupTitleNumber = new HashMap<>();

    /**
     * API Blueprint不允许出现重复的URI，因此，需要：
     * 缓存URI信息，出现重复时将其URI合并在一起进行定义
     */
    private Map<String, List<String>> uriRepeatMap = new HashMap<>();

    /**
     * 如果API已经添加，则不能重新添加；在URI重复时会出现这种情况
     */
    private Map<String, Boolean> apiAppendedMap = new HashMap<>();

    public DocsBuilder() {
        builder.append(VERSION_HEADER).append(LINE_BREAK);
    }

    /**
     * 进行预分析，识别出重复的URI
     *
     * @param apiProject
     */
    public void preAnalyze(ApiProject apiProject) {
        this.apiProject = apiProject;

        for (ApiGroup apiGroup : apiProject.getGroups()) {
            for (HttpApi httpApi : apiGroup.getHttpApis()) {
                String uri = getUri(httpApi.getPath(), httpApi.getUriParameters());
                List<String> ids = uriRepeatMap.get(uri);
                if (ids == null) {
                    ids = new ArrayList<>();
                    ids.add(httpApi.getId());
                    uriRepeatMap.put(uri, ids);
                } else {
                    ids.add(httpApi.getId());
                }
            }
        }
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
            String title = name;
            if (StringUtils.notBlank(version)) {
                title = String.format("%s %s", title, version);
            }
            Integer number = groupTitleNumber.get(title);
            if (number == null) {
                groupTitleNumber.put(title, 0);
            } else {
                groupTitleNumber.put(title, ++number);
                title = String.format("%s-%d", title, number);
            }
            builder.append(LINE_BREAK);
            builder.append(HASH_MARK).append(HASH_MARK).append(SPACE);
            builder.append(GROUP).append(SPACE).append(title).append(LINE_BREAK);
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
     * @param httpApi
     * @return
     */
    public DocsBuilder appendApi(HttpApi httpApi) {
        if (appendApi(httpApi.getName(), httpApi.getVersion(),
                httpApi.getHttpMethod(), httpApi.getPath(), httpApi.getUriParameters())) {
            appendApiDescription(httpApi.getDescription());
            appendPathParameters(httpApi.getPathParameters());
            appendUriParameters(httpApi.getUriParameters());
            appendHttpRequest(httpApi.getHttpRequest());
            appendHttpResponse(httpApi.getHttpResponse());
            appendErrorCodes(httpApi.getErrorCodes());
        }
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
     * 添加API
     * <ol>
     * <li>如果返回true，表示该API是普通的API，可以继续往下添加参数、Request、Response等</li>
     * <li>如果返回false，表示该API是聚合的API，不可以继续往下添加参数、Request、Response等</li>
     * </ol>
     *
     * @param name
     * @param version
     * @param httpMethod
     * @param path
     * @param uriParameters
     * @return
     */
    private boolean appendApi(String name, String version, HttpMethod httpMethod, String path,
                              List<Parameter> uriParameters) {
        String uri = getUri(path, uriParameters);
        Boolean appended = apiAppendedMap.get(uri);
        if (appended != null && appended) {
            return false;
        }

        apiAppendedMap.put(uri, true);
        List<String> ids = uriRepeatMap.get(uri);
        if (ids.size() == 1) {
            appendNormalApi(name, version, httpMethod, uri);
            return true;
        } else {
            appendAggregationApi(uri);
            return false;
        }
    }

    /**
     * 添加普通的API，区别于聚合的API
     *
     * @param name
     * @param version
     * @param httpMethod
     * @param uri
     */
    private void appendNormalApi(String name, String version, HttpMethod httpMethod, String uri) {
        builder.append(LINE_BREAK);
        builder.append(HASH_MARK).append(HASH_MARK).append(HASH_MARK).append(SPACE);
        builder.append(name);
        if (StringUtils.notBlank(version)) {
            builder.append(SPACE).append(version);
        }
        builder.append(SPACE).append(LEFT_BRACKET).append(httpMethod);
        builder.append(SPACE).append(uri);
        builder.append(RIGHT_BRACKET).append(LINE_BREAK);
    }

    /**
     * 添加聚合的API，多个API的URI出现重复，则将其定义在一起
     *
     * @param uri
     */
    private void appendAggregationApi(String uri) {
        builder.append(LINE_BREAK);
        builder.append(HASH_MARK).append(HASH_MARK).append(HASH_MARK);
        builder.append(SPACE).append(RESOURCE).append(SPACE);
        builder.append(LEFT_BRACKET).append(uri).append(RIGHT_BRACKET).append(LINE_BREAK);

        List<String> ids = uriRepeatMap.get(uri);
        for (String id : ids) {
            HttpApi httpApi = apiProject.getHttpApi(id);
            builder.append(LINE_BREAK);
            builder.append(HASH_MARK).append(HASH_MARK).append(HASH_MARK).append(HASH_MARK);
            builder.append(SPACE).append(httpApi.getName());
            if (StringUtils.notBlank(httpApi.getVersion())) {
                builder.append(SPACE).append(httpApi.getVersion());
            }
            builder.append(SPACE).append(LEFT_BRACKET).append(httpApi.getHttpMethod());
            builder.append(RIGHT_BRACKET).append(LINE_BREAK);

            // 为聚合的API添加参数、Request、Response等
            appendApiDescription(httpApi.getDescription());
            appendPathParameters(httpApi.getPathParameters());
            appendUriParameters(httpApi.getUriParameters());
            appendHttpRequest(httpApi.getHttpRequest());
            appendHttpResponse(httpApi.getHttpResponse());
            appendErrorCodes(httpApi.getErrorCodes());
        }
    }

    /**
     * 添加API描述
     *
     * @param description
     */
    private void appendApiDescription(String description) {
        if (StringUtils.notBlank(description)) {
            builder.append(LINE_BREAK);
            builder.append(description).append(LINE_BREAK);
        }
    }

    /**
     * 添加Http Path参数
     *
     * @param parameters
     */
    private void appendPathParameters(List<Parameter> parameters) {
        builder.append(parametersBuilder.buildParameters(parameters).build());
        parametersBuilder.reset();
    }

    /**
     * 添加Http Uri参数
     *
     * @param parameters
     */
    private void appendUriParameters(List<Parameter> parameters) {
        builder.append(parametersBuilder.buildParameters(parameters).build());
        parametersBuilder.reset();
    }

    /**
     * 添加HttpRequest
     *
     * @param httpRequest
     */
    private void appendHttpRequest(HttpRequest httpRequest) {
        if (httpRequest != null) {
            if (!FileHelper.hasFile(httpRequest.getBody())) {
                builder.append(LINE_BREAK);
                builder.append(REQUEST).append(SPACE);
                buildContentType(httpRequest.getHeaders());
                builder.append(LINE_BREAK);
                if (httpRequest.getHeaders() != null && !httpRequest.getHeaders().isEmpty()) {
                    buildHeaders(httpRequest.getHeaders());
                }
                List<Parameter> parameters = httpRequest.getBody();
                if (parameters != null && !parameters.isEmpty()) {
                    builder.append(attributesBuilder.buildAttributes(parameters).build());
                    attributesBuilder.reset();
                }
            } else {
                builder.append(fileRequestBuilder.buildRequest(httpRequest).build());
                fileRequestBuilder.reset();
            }
        }
    }

    /**
     * 添加HttpResponse
     *
     * @param httpResponse
     */
    private void appendHttpResponse(HttpResponse httpResponse) {
        if (httpResponse != null) {
            builder.append(LINE_BREAK);
            builder.append(RESPONSE).append(SPACE).append(httpResponse.getStatus());
            if (httpResponse.getStatus() == HttpStatus.SC_MOVED_TEMPORARILY) {
                builder.append(LINE_BREAK);
                if (httpResponse.getHeaders() != null && !httpResponse.getHeaders().isEmpty()) {
                    buildHeaders(httpResponse.getHeaders());
                }
                return;
            } else {
                builder.append(SPACE);
                buildContentType(httpResponse.getHeaders());
                builder.append(LINE_BREAK);
                if (httpResponse.getHeaders() != null && !httpResponse.getHeaders().isEmpty()) {
                    buildHeaders(httpResponse.getHeaders());
                }
            }
            if (httpResponse.getBody() != null && !httpResponse.getBody().isEmpty()) {
                Parameter returnParameter = httpResponse.getBody().get(0);
                switch (returnParameter.getType()) {
                    case OBJECT:
                    case ARRAY:
                        builder.append(attributesBuilder.buildAttributes(Arrays.asList(returnParameter)).build());
                        attributesBuilder.reset();
                        break;
                    case STRING:
                        buildResponseValue("Hello, world!");
                        break;
                    case NUMBER:
                        buildResponseValue(1);
                        break;
                    case BOOLEAN:
                        buildResponseValue(true);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * 添加错误码
     *
     * @param errorCodes
     */
    private void appendErrorCodes(List<ErrorCode> errorCodes) {
        // TODO append error code
    }

    /**
     * 获取API的URI
     *
     * @param path
     * @param uriParameters
     * @return
     */
    private String getUri(String path, List<Parameter> uriParameters) {
        StringBuilder stringBuilder = new StringBuilder(path);
        if (uriParameters != null && !uriParameters.isEmpty()) {
            stringBuilder.append(QUESTION_MARK);
            for (int i = 0; i < uriParameters.size(); i++) {
                Parameter parameter = uriParameters.get(i);
                stringBuilder.append(getQueryString(parameter, i == uriParameters.size() - 1, null));
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 获取API的QueryString
     *
     * @param parameter
     * @param last
     * @param suffix
     * @return
     */
    private String getQueryString(Parameter parameter, boolean last, String suffix) {
        StringBuilder stringBuilder = new StringBuilder();
        boolean isObject = parameter.getType() == ParameterType.OBJECT;
        boolean isObjectArray = parameter.getType() == ParameterType.ARRAY
                && parameter.getChildParameters().get(0).getType() == ParameterType.OBJECT;
        if (isObject || isObjectArray) {
            if (parameter.getParentParameter() != null
                    && parameter.getParentParameter().getType() != ParameterType.ARRAY) {
                if (suffix == null) {
                    suffix = parameter.getName();
                } else {
                    suffix = String.format("%s.%s", suffix, parameter.getName());
                }
            }
            List<Parameter> childParameters = parameter.getChildParameters();
            if (parameter.getChildParameters() != null) {
                for (int i = 0; i < childParameters.size(); i++) {
                    Parameter childParameter = childParameters.get(i);
                    stringBuilder.append(getQueryString(childParameter, i == childParameters.size() - 1, suffix));
                }
            } else {
                stringBuilder.append(suffix).append(EQUAL);
                stringBuilder.append(LEFT_BRACE).append(suffix).append(RIGHT_BRACE);
            }
        } else {
            String parameterName = parameter.getName();
            if (suffix != null) {
                parameterName = String.format("%s.%s", suffix, parameter.getName());
            }
            stringBuilder.append(parameterName).append(EQUAL);
            stringBuilder.append(LEFT_BRACE).append(parameterName).append(RIGHT_BRACE);
        }
        if (!last) {
            stringBuilder.append(AND_MARK);
        }
        return stringBuilder.toString();
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
            if (Constants.CONTENT_TYPE.equals(header.getName())) {
                continue;
            }
            builder.append(TAB).append(TAB).append(TAB).append(header.getName()).append(COLON);
            builder.append(SPACE).append(header.getDescription()).append(LINE_BREAK);
        }
    }

    /**
     * 构建Http返回值
     *
     * @param obj
     */
    private void buildResponseValue(Object obj) {
        builder.append(LINE_BREAK).append(TAB).append(TAB).append(obj).append(LINE_BREAK);
    }

}
