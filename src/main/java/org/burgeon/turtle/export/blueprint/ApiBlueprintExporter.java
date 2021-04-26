package org.burgeon.turtle.export.blueprint;

import lombok.extern.slf4j.Slf4j;
import org.burgeon.turtle.core.command.CommandExecutorFactory;
import org.burgeon.turtle.core.common.Constants;
import org.burgeon.turtle.core.event.BaseExportListener;
import org.burgeon.turtle.core.event.ExportEvent;
import org.burgeon.turtle.core.event.target.BlueprintEventTarget;
import org.burgeon.turtle.core.event.target.EventTarget;
import org.burgeon.turtle.core.model.api.*;
import org.burgeon.turtle.core.utils.EnvUtils;
import org.burgeon.turtle.export.blueprint.model.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 导出API Blueprint文档
 *
 * @author Sam Lu
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
        return EventTarget.fromClass(BlueprintEventTarget.class).getCode();
    }

    @Override
    public void action(ExportEvent exportEvent) {
        log.info("Export API Blueprint docs.");

        ApiProject apiProject = exportEvent.getApiProject();
        apiProject = buildApiProjectProxy(apiProject);

        List<ApiGroup> apiGroups = apiProject.getGroups();
        log.debug("Export {} group.", apiGroups.size());
        int apiSize = 0;
        for (ApiGroup apiGroup : apiGroups) {
            List<HttpApi> httpApis = apiGroup.getHttpApis();
            apiSize += httpApis.size();
        }
        log.debug("Export {} api.", apiSize);

        try {
            export(apiProject, exportEvent.getSourceCode());
        } catch (Exception e) {
            if (EnvUtils.getBooleanProperty(Constants.DEBUG, false)) {
                e.printStackTrace();
            }
            throw new RuntimeException("Export API Blueprint docs fail.");
        }
    }

    /**
     * 构建【API项目-代理】
     *
     * @param apiProject
     * @return
     */
    private ApiProject buildApiProjectProxy(ApiProject apiProject) {
        List<ApiGroup> apiGroupProxyList = null;
        Map<String, ApiGroup> apiGroupProxyMap = new HashMap<>(apiProject.getApiGroupMap().size());
        Map<String, HttpApi> httpApiProxyMap = new HashMap<>(apiProject.getHttpApiMap().size());
        Map<String, Parameter> parameterProxyMap = new HashMap<>(apiProject.getParameterMap().size());
        if (apiProject.getGroups() != null) {
            apiGroupProxyList = new ArrayList<>(apiProject.getGroups().size());
            for (ApiGroup apiGroup : apiProject.getGroups()) {
                List<HttpApi> httpApiProxyList = null;
                if (apiGroup.getHttpApis() != null) {
                    httpApiProxyList = new ArrayList<>(apiGroup.getHttpApis().size());
                    for (HttpApi httpApi : apiGroup.getHttpApis()) {
                        List<Parameter> pathParameterProxyList = buildParameterProxyList(parameterProxyMap,
                                httpApi.getPathParameters());
                        List<Parameter> uriParameterProxyList = buildParameterProxyList(parameterProxyMap,
                                httpApi.getUriParameters());
                        HttpRequest httpRequestProxy = buildRequestProxy(parameterProxyMap,
                                httpApi.getHttpRequest());
                        HttpResponse httpResponseProxy = buildResponseProxy(parameterProxyMap,
                                httpApi.getHttpResponse());
                        HttpApi httpApiProxy = new HttpApiProxy(httpApi, pathParameterProxyList,
                                uriParameterProxyList, httpRequestProxy, httpResponseProxy);
                        httpApiProxyList.add(httpApiProxy);
                        httpApiProxyMap.put(httpApiProxy.getId(), httpApiProxy);
                    }
                }
                ApiGroup apiGroupProxy = new ApiGroupProxy(apiGroup, httpApiProxyList);
                apiGroupProxyList.add(apiGroupProxy);
                apiGroupProxyMap.put(apiGroupProxy.getId(), apiGroupProxy);
            }
        }
        ApiProject apiProjectProxy = new ApiProjectProxy(apiProject, apiGroupProxyList,
                apiGroupProxyMap, httpApiProxyMap, parameterProxyMap);
        return apiProjectProxy;
    }

    /**
     * 构建参数列表的代理
     *
     * @param parameterMap
     * @param parameters
     */
    private List<Parameter> buildParameterProxyList(Map<String, Parameter> parameterMap,
                                                    List<Parameter> parameters) {
        if (parameters == null) {
            return null;
        }
        List<Parameter> parameterProxyList = new ArrayList<>(parameters.size());
        for (Parameter parameter : parameters) {
            Parameter parameterProxy = buildParameterProxy(parameterMap, null, parameter);
            parameterProxyList.add(parameterProxy);
        }
        return parameterProxyList;
    }

    /**
     * 构建HttpRequest的Header和Parameter的代理
     *
     * @param parameterMap
     * @param httpRequest
     */
    private HttpRequest buildRequestProxy(Map<String, Parameter> parameterMap, HttpRequest httpRequest) {
        if (httpRequest == null) {
            return null;
        }
        HttpRequest httpHttpRequestProxy = new HttpRequest();
        httpHttpRequestProxy.setHeaders(buildHeaderProxyList(httpRequest.getHeaders()));
        httpHttpRequestProxy.setBody(buildParameterProxyList(parameterMap, httpRequest.getBody()));
        return httpHttpRequestProxy;
    }

    /**
     * 构建HttpResponse的Header和Parameter的代理
     *
     * @param parameterMap
     * @param httpResponse
     */
    private HttpResponse buildResponseProxy(Map<String, Parameter> parameterMap, HttpResponse httpResponse) {
        if (httpResponse == null) {
            return null;
        }
        HttpResponse httpResponseProxy = new HttpResponse();
        httpResponseProxy.setHeaders(buildHeaderProxyList(httpResponse.getHeaders()));
        httpResponseProxy.setBody(buildParameterProxyList(parameterMap, httpResponse.getBody()));
        httpResponseProxy.setStatus(httpResponse.getStatus());
        return httpResponseProxy;
    }

    /**
     * 构建HTTP头列表的代理
     *
     * @param headers
     */
    private List<HttpHeader> buildHeaderProxyList(List<HttpHeader> headers) {
        if (headers == null) {
            return null;
        }
        List<HttpHeader> headersProxyList = new ArrayList<>(headers.size());
        for (HttpHeader header : headers) {
            HttpHeader headerProxy = buildHeaderProxy(header);
            headersProxyList.add(headerProxy);
        }
        return headersProxyList;
    }

    /**
     * 构建【HTTP头-代理】
     *
     * @param header
     */
    private HttpHeader buildHeaderProxy(HttpHeader header) {
        return new HttpHeaderProxy(header);
    }

    /**
     * 构建【参数-代理】
     *
     * @param parameterMap
     * @param parentParameter
     * @param parameter
     */
    private Parameter buildParameterProxy(Map<String, Parameter> parameterMap, Parameter parentParameter,
                                          Parameter parameter) {
        Parameter parameterProxy = new ParameterProxy(parameter);
        parameterProxy.setParentParameter(parentParameter);
        List<Parameter> childParameterProxyList = null;
        if (parameter.getChildParameters() != null) {
            childParameterProxyList = new ArrayList<>(parameter.getChildParameters().size());
            for (Parameter childParameter : parameter.getChildParameters()) {
                Parameter childParameterProxy = buildParameterProxy(parameterMap, parameterProxy,
                        childParameter);
                childParameterProxyList.add(childParameterProxy);
                parameterMap.put(childParameterProxy.getId(), childParameterProxy);
            }
        }
        parameterProxy.setChildParameters(childParameterProxyList);
        parameterMap.put(parameterProxy.getId(), parameterProxy);
        return parameterProxy;
    }

    /**
     * 导出API文档
     *
     * @param apiProject
     * @param sourceCode
     */
    private void export(ApiProject apiProject, int sourceCode) throws IOException {
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
        generateDocs(sourceCode);
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
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * 生成API文档
     *
     * @param sourceCode
     */
    private void generateDocs(int sourceCode) {
        String targetPath = EnvUtils.getStringProperty(Constants.TARGET_PATH);
        String command = String.format("aglio -i %s/api-blueprint.apib -o %s/api-blueprint.html",
                targetPath, targetPath);
        try {
            CommandExecutorFactory.getCommandExecutor(sourceCode)
                    .execute(command);
        } catch (Exception e) {
            log.error("Generate docs fail.", e);
        }
    }

}
