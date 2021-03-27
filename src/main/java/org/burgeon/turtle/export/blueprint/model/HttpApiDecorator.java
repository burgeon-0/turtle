package org.burgeon.turtle.export.blueprint.model;

import org.burgeon.turtle.core.common.Constants;
import org.burgeon.turtle.core.model.api.*;
import org.burgeon.turtle.export.blueprint.FilterHelper;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * HTTP接口【装饰器】
 *
 * @author luxiaocong
 * @createdOn 2021/3/27
 */
public class HttpApiDecorator extends HttpApi {

    private static final String PATH_PARAMETER_REGEX = "\\{.*?-.*?\\}";

    private HttpApi httpApi;
    private List<Parameter> pathParameters;
    private List<Parameter> uriParameters;
    private HttpRequest httpRequest;
    private HttpResponse httpResponse;

    private String path;

    public HttpApiDecorator(HttpApi httpApi, List<Parameter> pathParameters, List<Parameter> uriParameters,
                            HttpRequest httpRequest, HttpResponse httpResponse) {
        this.httpApi = httpApi;
        this.pathParameters = pathParameters;
        this.uriParameters = uriParameters;
        this.httpRequest = httpRequest;
        this.httpResponse = httpResponse;
    }

    @Override
    public String getId() {
        return httpApi.getId();
    }

    @Override
    public String getName() {
        return FilterHelper.filterName(httpApi.getName());
    }

    @Override
    public String getDescription() {
        return FilterHelper.filterDescription(httpApi.getDescription());
    }

    @Override
    public String getVersion() {
        return httpApi.getVersion();
    }

    @Override
    public HttpMethod getHttpMethod() {
        return httpApi.getHttpMethod();
    }

    /**
     * 在API Blueprint的规范中，path参数名称不允许有"-"，如有，在此替换为"_"
     *
     * @return
     */
    @Override
    public String getPath() {
        if (path == null) {
            path = httpApi.getPath();
            Pattern pattern = Pattern.compile(PATH_PARAMETER_REGEX);
            Matcher matcher = pattern.matcher(path);
            if (matcher.find()) {
                do {
                    String parameterName = matcher.group();
                    String replaceParameterName = parameterName.replaceAll(Constants.MINUS, Constants.UNDERLINE);
                    path = path.replace(parameterName, replaceParameterName);
                } while (matcher.find());
            }
        }
        return path;
    }

    @Override
    public List<Parameter> getPathParameters() {
        return pathParameters;
    }

    @Override
    public List<Parameter> getUriParameters() {
        return uriParameters;
    }

    @Override
    public HttpRequest getHttpRequest() {
        return httpRequest;
    }

    @Override
    public HttpResponse getHttpResponse() {
        return httpResponse;
    }

    @Override
    public List<ErrorCode> getErrorCodes() {
        return httpApi.getErrorCodes();
    }

}
