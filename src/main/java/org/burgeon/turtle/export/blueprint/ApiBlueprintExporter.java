package org.burgeon.turtle.export.blueprint;

import lombok.extern.slf4j.Slf4j;
import org.burgeon.turtle.core.common.Constants;
import org.burgeon.turtle.core.event.BaseExportListener;
import org.burgeon.turtle.core.event.EventTarget;
import org.burgeon.turtle.core.event.ExportEvent;
import org.burgeon.turtle.core.model.api.*;
import org.burgeon.turtle.core.utils.EnvUtils;
import org.burgeon.turtle.export.blueprint.model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 导出API Blueprint文档
 *
 * @author luxiaocong
 * @createdOn 2021/2/26
 */
@Slf4j
public class ApiBlueprintExporter extends BaseExportListener {

    private static final ApiBlueprintExporter INSTANCE = new ApiBlueprintExporter();

    private ApiBlueprintExporter() {}

    public static ApiBlueprintExporter getInstance() {
        return INSTANCE;
    }

    @Override
    public int targetCode() {
        return EventTarget.BLUEPRINT.getCode();
    }

    @Override
    public void action(ExportEvent exportEvent) {
        log.info("Export API Blueprint docs.");

        ApiProject apiProject = exportEvent.getApiProject();
        apiProject = buildApiProjectDecorator(apiProject);

        List<ApiGroup> apiGroups = apiProject.getGroups();
        log.debug("Export {} group.", apiGroups.size());
        int apiSize = 0;
        for (ApiGroup apiGroup : apiGroups) {
            List<HttpApi> httpApis = apiGroup.getHttpApis();
            apiSize += httpApis.size();
        }
        log.debug("Export {} api.", apiSize);

        try {
            export(apiProject);
        } catch (Exception e) {
            if (EnvUtils.getBooleanProperty(Constants.DEBUG, false)) {
                e.printStackTrace();
            }
            throw new RuntimeException("Export API Blueprint docs fail.");
        }
    }

    /**
     * 构建【API项目-装饰器】
     *
     * @param apiProject
     * @return
     */
    private ApiProject buildApiProjectDecorator(ApiProject apiProject) {
        List<ApiGroup> apiGroupDecorators = null;
        Map<String, ApiGroup> apiGroupDecoratorMap = new HashMap<>(apiProject.getApiGroupMap().size());
        Map<String, HttpApi> httpApiDecoratorMap = new HashMap<>(apiProject.getHttpApiMap().size());
        Map<String, Parameter> parameterDecoratorMap = new HashMap<>(apiProject.getParameterMap().size());
        if (apiProject.getGroups() != null) {
            apiGroupDecorators = new ArrayList<>(apiProject.getGroups().size());
            for (ApiGroup apiGroup : apiProject.getGroups()) {
                List<HttpApi> httpApiDecorators = null;
                if (apiGroup.getHttpApis() != null) {
                    httpApiDecorators = new ArrayList<>(apiGroup.getHttpApis().size());
                    for (HttpApi httpApi : apiGroup.getHttpApis()) {
                        List<Parameter> pathParameterDecorators = buildParameterDecorators(parameterDecoratorMap,
                                httpApi.getPathParameters());
                        List<Parameter> uriParameterDecorators = buildParameterDecorators(parameterDecoratorMap,
                                httpApi.getUriParameters());
                        HttpRequest httpRequestDecorator = buildRequestDecorator(parameterDecoratorMap,
                                httpApi.getHttpRequest());
                        HttpResponse httpResponseDecorator = buildResponseDecorator(parameterDecoratorMap,
                                httpApi.getHttpResponse());
                        HttpApi httpApiDecorator = new HttpApiDecorator(httpApi, pathParameterDecorators,
                                uriParameterDecorators, httpRequestDecorator, httpResponseDecorator);
                        httpApiDecorators.add(httpApiDecorator);
                        httpApiDecoratorMap.put(httpApiDecorator.getId(), httpApiDecorator);
                    }
                }
                ApiGroup apiGroupDecorator = new ApiGroupDecorator(apiGroup, httpApiDecorators);
                apiGroupDecorators.add(apiGroupDecorator);
                apiGroupDecoratorMap.put(apiGroupDecorator.getId(), apiGroupDecorator);
            }
        }
        ApiProject apiProjectDecorator = new ApiProjectDecorator(apiProject, apiGroupDecorators,
                apiGroupDecoratorMap, httpApiDecoratorMap, parameterDecoratorMap);
        return apiProjectDecorator;
    }

    /**
     * 构建参数列表的装饰器
     *
     * @param parameterMap
     * @param parameters
     */
    private List<Parameter> buildParameterDecorators(Map<String, Parameter> parameterMap,
                                                     List<Parameter> parameters) {
        if (parameters == null) {
            return null;
        }
        List<Parameter> parameterDecorators = new ArrayList<>(parameters.size());
        for (Parameter parameter : parameters) {
            Parameter parameterDecorator = buildParameterDecorator(parameterMap, null, parameter);
            parameterDecorators.add(parameterDecorator);
        }
        return parameterDecorators;
    }

    /**
     * 构建HttpRequest的Header和Parameter的装饰器
     *
     * @param parameterMap
     * @param httpRequest
     */
    private HttpRequest buildRequestDecorator(Map<String, Parameter> parameterMap, HttpRequest httpRequest) {
        if (httpRequest == null) {
            return null;
        }
        HttpRequest httpHttpRequestDecorator = new HttpRequest();
        httpHttpRequestDecorator.setHeaders(buildHeaderDecorators(httpRequest.getHeaders()));
        httpHttpRequestDecorator.setBody(buildParameterDecorators(parameterMap, httpRequest.getBody()));
        return httpHttpRequestDecorator;
    }

    /**
     * 构建HttpResponse的Header和Parameter的装饰器
     *
     * @param parameterMap
     * @param httpResponse
     */
    private HttpResponse buildResponseDecorator(Map<String, Parameter> parameterMap, HttpResponse httpResponse) {
        if (httpResponse == null) {
            return null;
        }
        HttpResponse httpResponseDecorator = new HttpResponse();
        httpResponseDecorator.setHeaders(buildHeaderDecorators(httpResponse.getHeaders()));
        httpResponseDecorator.setBody(buildParameterDecorators(parameterMap, httpResponse.getBody()));
        httpResponseDecorator.setStatus(httpResponse.getStatus());
        return httpResponseDecorator;
    }

    /**
     * 构建HTTP头列表的装饰器
     *
     * @param headers
     */
    private List<HttpHeader> buildHeaderDecorators(List<HttpHeader> headers) {
        if (headers == null) {
            return null;
        }
        List<HttpHeader> headersDecorators = new ArrayList<>(headers.size());
        for (HttpHeader header : headers) {
            HttpHeader headerDecorator = buildHeaderDecorator(header);
            headersDecorators.add(headerDecorator);
        }
        return headersDecorators;
    }

    /**
     * 构建【HTTP头-装饰器】
     *
     * @param header
     */
    private HttpHeader buildHeaderDecorator(HttpHeader header) {
        return new HttpHeaderDecorator(header);
    }

    /**
     * 构建【参数-装饰器】
     *
     * @param parameterMap
     * @param parentParameter
     * @param parameter
     */
    private Parameter buildParameterDecorator(Map<String, Parameter> parameterMap, Parameter parentParameter,
                                              Parameter parameter) {
        Parameter parameterDecorator = new ParameterDecorator(parameter);
        parameterDecorator.setParentParameter(parentParameter);
        List<Parameter> childParameterDecorators = null;
        if (parameter.getChildParameters() != null) {
            childParameterDecorators = new ArrayList<>(parameter.getChildParameters().size());
            for (Parameter childParameter : parameter.getChildParameters()) {
                Parameter childParameterDecorator = buildParameterDecorator(parameterMap, parameterDecorator,
                        childParameter);
                childParameterDecorators.add(childParameterDecorator);
                parameterMap.put(childParameterDecorator.getId(), childParameterDecorator);
            }
        }
        parameterDecorator.setChildParameters(childParameterDecorators);
        parameterMap.put(parameterDecorator.getId(), parameterDecorator);
        return parameterDecorator;
    }

    /**
     * 导出API文档
     *
     * @param apiProject
     */
    private void export(ApiProject apiProject) throws IOException {
        DocsBuilder docsBuilder = new DocsBuilder();
        docsBuilder.preAnalyze(apiProject);
        docsBuilder.appendHost(apiProject.getHost());
        docsBuilder.appendTitle(apiProject.getName());
        docsBuilder.appendDescription(apiProject.getDescription());
        for (ApiGroup apiGroup : apiProject.getGroups()) {
            docsBuilder.appendGroupTitle(apiGroup.getName(), apiGroup.getVersion());
            docsBuilder.appendGroupDescription(apiGroup.getDescription());
            for (HttpApi httpApi : apiGroup.getHttpApis()) {
                docsBuilder.appendApi(httpApi);
            }
        }

        writeToFile(docsBuilder.build());
    }

    /**
     * 将API内容写入到文件
     *
     * @param content
     * @throws IOException
     */
    private void writeToFile(String content) throws IOException {
        String targetPath = EnvUtils.getStringProperty(Constants.TARGET_PATH);
        String targetFile = targetPath + Constants.SEPARATOR_FILE + "api-blueprint.apib";
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(targetFile));
            writer.write(content);
            System.out.println(content);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

}
