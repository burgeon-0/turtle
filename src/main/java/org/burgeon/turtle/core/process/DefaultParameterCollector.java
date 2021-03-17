package org.burgeon.turtle.core.process;

import org.burgeon.turtle.core.model.api.*;
import org.burgeon.turtle.core.model.source.SourceProject;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 默认参数收集器
 *
 * @author luxiaocong
 * @createdOn 2021/3/14
 */
public class DefaultParameterCollector implements Collector {

    /**
     * 排除项，无需解析、收集的参数
     */
    private List<String> exclusions = new ArrayList<>();
    private List<String> exclusionPatterns = new ArrayList<>();

    {
        exclusions.add("org.springframework.validation.*");
        exclusions.add("javax.servlet.**");
    }

    private Processor processor;

    public DefaultParameterCollector(Processor processor) {
        this.processor = processor;
    }

    /**
     * 添加排除项
     *
     * @param exclusion
     */
    public void addExclusion(String exclusion) {
        exclusions.add(exclusion);
    }

    /**
     * 移除排除项
     *
     * @param exclusion
     */
    public void removeExclusion(String exclusion) {
        exclusions.remove(exclusion);
    }

    /**
     * 初始化排除项的正则表达式
     */
    private void initExclusionPatterns() {
        for (String exclusion : exclusions) {
            if (exclusion.endsWith("**")) {
                exclusion = exclusion.replace("**", "[\\$|_|A-Z|a-z]([\\$|_|0-9|A-Z|a-z])*(\\.[\\$|_|A-Z|a-z]([\\$|_|0-9|A-Z|a-z])*)*");
            } else if (exclusion.endsWith("*")) {
                exclusion = exclusion.replace("*", "[\\$|_|A-Z|a-z]([\\$|_|0-9|A-Z|a-z])*");
            }
            exclusion = String.format("^%s$", exclusion);
            exclusionPatterns.add(exclusion);
        }
    }

    @Override
    public void collect(ApiProject apiProject, SourceProject sourceProject, CollectorContext context) {
        initExclusionPatterns();

        List<ApiGroup> apiGroups = apiProject.getGroups();
        for (ApiGroup group : apiGroups) {
            List<HttpApi> httpApis = group.getHttpApis();
            for (HttpApi httpApi : httpApis) {
                CtMethod<?> ctMethod = sourceProject.getCtMethod(httpApi.getId());
                List<CtParameter<?>> ctParameters = ctMethod.getParameters();
                System.out.println(httpApi.getHttpMethod() + ": " + httpApi.getPath());
                for (CtParameter<?> ctParameter : ctParameters) {
                    parseParameter(httpApi, ctParameter);
                }
            }
        }

        context.collectNext(apiProject, sourceProject);
    }

    /**
     * 解析参数
     *
     * @param httpApi
     * @param ctParameter
     */
    private void parseParameter(HttpApi httpApi, CtParameter<?> ctParameter) {
        String type = ctParameter.getType().getQualifiedName();
        for (String exclusion : exclusionPatterns) {
            if (Pattern.matches(exclusion, type)) {
                return;
            }
        }

        List<CtAnnotation<?>> ctAnnotations = ctParameter.getAnnotations();
        if (isRequestParameter(httpApi, ctAnnotations)) {
            initUriParameters(httpApi);
            httpApi.getPathParameters().addAll(buildParameters(ctParameter, ctAnnotations));
        } else {
            initHttpRequest(httpApi);
            httpApi.getHttpRequest().getBody().addAll(buildParameters(ctParameter, ctAnnotations));
        }
        //System.out.println(ctParameter.getType().getQualifiedName());
    }

    /**
     * 判断是否是URL参数
     *
     * @param httpApi
     * @param ctAnnotations
     * @return
     */
    private boolean isRequestParameter(HttpApi httpApi, List<CtAnnotation<?>> ctAnnotations) {
        if (ctAnnotations == null) {
            if (httpApi.getHttpMethod().equals(HttpMethod.POST)
                    || httpApi.getHttpMethod().equals(HttpMethod.PUT)
                    || httpApi.getHttpMethod().equals(HttpMethod.DELETE)
                    || httpApi.getHttpMethod().equals(HttpMethod.PATCH)) {
                return false;
            } else {
                return true;
            }
        }
        // TODO
        return false;
    }

    /**
     * 初始化请求URI参数
     *
     * @param httpApi
     */
    private void initUriParameters(HttpApi httpApi) {
        List<Parameter> parameters = httpApi.getPathParameters();
        if (parameters == null) {
            parameters = new ArrayList<>();
            httpApi.setUriParameters(parameters);
        }
    }

    /**
     * 初始化请求Body参数
     *
     * @param httpApi
     */
    private void initHttpRequest(HttpApi httpApi) {
        HttpRequest httpRequest = httpApi.getHttpRequest();
        if (httpRequest == null) {
            httpRequest = new HttpRequest();
            httpApi.setHttpRequest(httpRequest);
            httpRequest.setBody(new ArrayList<>());
        } else if (httpRequest.getBody() == null) {
            httpRequest.setBody(new ArrayList<>());
        }
    }

    /**
     * 构建请求参数
     *
     * @param parameter
     * @param ctAnnotations
     * @return
     */
    private List<Parameter> buildParameters(CtParameter<?> parameter, List<CtAnnotation<?>> ctAnnotations) {
        List<Parameter> parameters = new ArrayList<>();
        Parameter param = new Parameter();
        parameters.add(param);

        String typeName = parameter.getType().getQualifiedName();
        ParameterType type;
        switch (typeName) {
            case "byte":
            case "short":
            case "int":
            case "long":
            case "float":
            case "double":
                type = ParameterType.NUMBER;
                break;
            case "boolean":
                type = ParameterType.BOOLEAN;
                break;
            case "char":
                type = ParameterType.STRING;
                break;
            default:
                type = buildParameters(parameters, parameter, typeName);
                break;
        }

        param.setType(type);
        param.setName(parameter.getSimpleName());
        return parameters;
    }

    /**
     * 构建请求参数
     *
     * @param parameters
     * @param parameter
     * @param typeName
     * @return
     */
    private ParameterType buildParameters(List<Parameter> parameters, CtParameter<?> parameter,
                                          String typeName) {
        ParameterType type;
        try {
            Class<?> clazz = Class.forName(typeName);
            System.out.println(parameter.getSimpleName() + ": " + typeName);
            Object obj = clazz.newInstance();
            JsonConverter jsonConverter = processor.getJsonConverter();
            String json = jsonConverter.convert(obj);
            System.out.println(parameter.getSimpleName() + ": " + json);
            Object newObj = jsonConverter.convert(json);
            Class<?> newObjClass = newObj.getClass();
            type = jsonConverter.inferParameterType(newObjClass);
        } catch (Exception e) {
            type = ParameterType.OBJECT;
        }
        return type;
    }

}
