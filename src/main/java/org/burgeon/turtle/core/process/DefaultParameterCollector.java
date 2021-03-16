package org.burgeon.turtle.core.process;

import org.burgeon.turtle.core.model.api.*;
import org.burgeon.turtle.core.model.source.SourceProject;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 默认参数收集器
 *
 * @author luxiaocong
 * @createdOn 2021/3/14
 */
public class DefaultParameterCollector implements Collector {

    private static final String PRIMITIVE_BYTE = "byte";
    private static final String PRIMITIVE_SHORT = "short";
    private static final String PRIMITIVE_INT = "int";
    private static final String PRIMITIVE_LONG = "long";
    private static final String PRIMITIVE_FLOAT = "float";
    private static final String PRIMITIVE_DOUBLE = "double";
    private static final String PRIMITIVE_BOOLEAN = "boolean";
    private static final String PRIMITIVE_CHAR = "char";
    private static final String BYTE = "java.lang.Byte";
    private static final String SHORT = "java.lang.Short";
    private static final String INTEGER = "java.lang.Integer";
    private static final String LONG = "java.lang.Long";
    private static final String FLOAT = "java.lang.Float";
    private static final String DOUBLE = "java.lang.Double";
    private static final String BOOLEAN = "java.lang.Boolean";
    private static final String CHARACTER = "java.lang.Character";
    private static final String STRING = "java.lang.String";
    private static final String NUMBER = "java.lang.Number";
    private static final String BIG_INTEGER = "java.math.BigInteger";
    private static final String BIG_DECIMAL = "java.math.BigDecimal";
    private static final String UUID = "java.lang.UUID";
    private static final String DATE = "java.util.Date";
    private static final String TIMESTAMP = "java.sql.Timestamp";
    private static final String LOCAL_DATE = "java.time.LocalDate";
    private static final String LOCAL_TIME = "java.time.LocalTime";
    private static final String LOCAL_DATE_TIME = "java.time.LocalDateTime";

    /**
     * 排除项，无需解析、收集的参数
     */
    private List<String> exclusions = new ArrayList<>();
    private List<String> exclusionPatterns = new ArrayList<>();

    {
        exclusions.add("org.springframework.validation.*");
        exclusions.add("javax.servlet.**");
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
            initRequestParameters(httpApi);
            httpApi.getRequestParameters().addAll(buildParameters(ctParameter, ctAnnotations));
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
     * 初始化URL参数
     *
     * @param httpApi
     */
    private void initRequestParameters(HttpApi httpApi) {
        List<Parameter> parameters = httpApi.getRequestParameters();
        if (parameters == null) {
            parameters = new ArrayList<>();
            httpApi.setRequestParameters(parameters);
        }
    }

    /**
     * 初始化Body参数
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
        param.setType(getParameterType(parameter));
        parameters.add(param);
        return parameters;
    }

    /**
     * 获取参数类型
     *
     * @param parameter
     * @return
     */
    private ParameterType getParameterType(CtParameter<?> parameter) {
        String type = parameter.getType().getQualifiedName();
        try {
            Class<?> clazz = Class.forName(type);
            char c = 'a';
            String str = "a";
            Date date = new Date();
            if (clazz.isInstance(0)) {
                return ParameterType.NUMBER;
            } else if (clazz.isInstance(true)) {
                return ParameterType.BOOLEAN;
            } else if (clazz.isInstance(c)) {
                return ParameterType.STRING;
            } else if (clazz.isInstance(str)) {
                return ParameterType.STRING;
            } else if (clazz.isInstance(date)) {
                return ParameterType.STRING;
            }
        } catch (Exception e) {
            return ParameterType.OBJECT;
        }
        return ParameterType.OBJECT;
    }

}
