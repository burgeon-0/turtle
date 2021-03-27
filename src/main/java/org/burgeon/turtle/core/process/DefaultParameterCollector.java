package org.burgeon.turtle.core.process;

import org.burgeon.turtle.core.common.CtModelHelper;
import org.burgeon.turtle.core.model.api.*;
import org.burgeon.turtle.core.model.source.SourceProject;
import spoon.reflect.declaration.*;
import spoon.reflect.path.CtRole;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.support.reflect.reference.CtArrayTypeReferenceImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 默认参数收集器
 *
 * @author luxiaocong
 * @createdOn 2021/3/14
 */
public class DefaultParameterCollector implements Collector {

    private static final String PUBLIC = "public";
    private static final String PROTECTED = "protected";
    private static final String PRIVATE = "private";

    private static final String VALID = "javax.validation.Valid";
    private static final String PATH_VARIABLE = "org.springframework.web.bind.annotation.PathVariable";
    private static final String REQUEST_PARAM = "org.springframework.web.bind.annotation.RequestParam";
    private static final String MODEL_ATTRIBUTE = "org.springframework.web.bind.annotation.ModelAttribute";
    private static final String REQUEST_BODY = "org.springframework.web.bind.annotation.RequestBody";

    private static final String NOT_NULL = "NotNull";
    private static final String NOT_BLANK = "NotBlank";
    private static final String NOT_EMPTY = "NotEmpty";

    private static final String JAVAX_VALIDATION = "javax.validation.constraints";
    private static final String HIBERNATE_VALIDATOR = "org.hibernate.validator.constraints";

    /**
     * 排除项，无需解析、收集的参数
     */
    private List<String> exclusions = new ArrayList<>();
    private List<String> exclusionPatterns = new ArrayList<>();

    {
        exclusions.add("org.springframework.validation.*");
        exclusions.add("javax.servlet.**");
    }

    private ParameterTypeHandlerChain parameterTypeHandlerChain = new ParameterTypeHandlerChain();

    /**
     * 添加排除项
     *
     * @param exclusion 标识无需解析、收集的参数，可以使用通配符"*"、"**"，"*"代表任何类，"**"代表任何包下的任何类
     */
    public void addExclusion(String exclusion) {
        exclusions.add(exclusion);
    }

    /**
     * 移除排除项
     *
     * @param exclusion 标识无需解析、收集的参数，可以使用通配符"*"、"**"，"*"代表任何类，"**"代表任何包下的任何类
     */
    public void removeExclusion(String exclusion) {
        exclusions.remove(exclusion);
    }

    /**
     * 添加参数类型推断器
     *
     * @param handler 参数类型推断器，对于默认识别不了的参数类型，可以通过增加推断器的方式进行识别
     */
    public void addParameterTypeHandler(ParameterTypeHandler handler) {
        parameterTypeHandlerChain.addParameterTypeHandler(handler);
    }

    /**
     * 移除参数类型推断器
     *
     * @param handler 参数类型推断器，对于默认识别不了的参数类型，可以通过增加推断器的方式进行识别
     */
    public void removeParameterTypeHandler(ParameterTypeHandler handler) {
        parameterTypeHandlerChain.removeParameterTypeHandler(handler);
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
                    collectRequestParameter(apiProject, sourceProject, httpApi, ctParameter);
                }
                collectResponseParameter(apiProject, sourceProject, httpApi, ctMethod);
            }
        }

        context.collectNext(apiProject, sourceProject);
    }

    /**
     * 收集Request参数
     *
     * @param apiProject
     * @param sourceProject
     * @param httpApi
     * @param ctParameter
     */
    private void collectRequestParameter(ApiProject apiProject, SourceProject sourceProject,
                                         HttpApi httpApi, CtParameter<?> ctParameter) {
        String type = ctParameter.getType().getQualifiedName();
        for (String exclusion : exclusionPatterns) {
            if (Pattern.matches(exclusion, type)) {
                return;
            }
        }

        Parameter parameter = buildParameter(apiProject, sourceProject,
                httpApi, null, ParameterPosition.NORMAL, ctParameter);
        HttpParameterType httpParameterType = getHttpParameterType(httpApi,
                ctParameter.getAnnotations());
        httpParameterType = checkHttpParameterType(httpApi, ctParameter.getAnnotations(),
                httpParameterType, parameter);
        switch (httpParameterType) {
            case PATH_PARAMETER:
                initPathParameters(httpApi);
                String pathParameterName = getPathParameterName(ctParameter.getAnnotations());
                if (!"".equals(pathParameterName)) {
                    parameter.setName(pathParameterName);
                }
                httpApi.getPathParameters().add(parameter);
                break;
            case URI_PARAMETER:
                initUriParameters(httpApi);
                httpApi.getUriParameters().add(parameter);
                break;
            case BODY_PARAMETER:
                initHttpRequest(httpApi);
                httpApi.getHttpRequest().getBody().add(parameter);
                break;
            default:
                break;
        }
    }

    /**
     * 判断HTTP参数类型
     *
     * @param httpApi
     * @param ctAnnotations
     * @return
     * @see HttpParameterType
     */
    private HttpParameterType getHttpParameterType(HttpApi httpApi, List<CtAnnotation<?>> ctAnnotations) {
        boolean nullPositionMark = ctAnnotations == null || (ctAnnotations.size() == 1
                && VALID.equals(ctAnnotations.get(0).getType().getQualifiedName()));
        if (nullPositionMark) {
            if (httpApi.getHttpMethod().equals(HttpMethod.POST)
                    || httpApi.getHttpMethod().equals(HttpMethod.PUT)
                    || httpApi.getHttpMethod().equals(HttpMethod.DELETE)
                    || httpApi.getHttpMethod().equals(HttpMethod.PATCH)) {
                return HttpParameterType.BODY_PARAMETER;
            } else {
                return HttpParameterType.URI_PARAMETER;
            }
        }

        for (CtAnnotation<?> ctAnnotation : ctAnnotations) {
            String name = ctAnnotation.getType().getQualifiedName();
            switch (name) {
                case PATH_VARIABLE:
                    return HttpParameterType.PATH_PARAMETER;
                case REQUEST_PARAM:
                case MODEL_ATTRIBUTE:
                    return HttpParameterType.URI_PARAMETER;
                case REQUEST_BODY:
                    return HttpParameterType.BODY_PARAMETER;
                default:
                    break;
            }
        }
        return HttpParameterType.BODY_PARAMETER;
    }

    /**
     * 重新判断HTTP参数类型
     * <ol>
     * <li>
     * 如果参数类型为Path参数，但是路径上又不存在该参数，则重新判定为URI参数
     * </li>
     * </ol>
     *
     * @param httpApi
     * @param ctAnnotations
     * @param httpParameterType
     * @param parameter
     * @return
     */
    private HttpParameterType checkHttpParameterType(HttpApi httpApi,
                                                     List<CtAnnotation<?>> ctAnnotations,
                                                     HttpParameterType httpParameterType,
                                                     Parameter parameter) {
        if (httpParameterType == HttpParameterType.PATH_PARAMETER) {
            String pathParameterName = getPathParameterName(ctAnnotations);
            pathParameterName = "".equals(pathParameterName) ? parameter.getName() : pathParameterName;
            String str = String.format("{%s}", pathParameterName);
            if (!httpApi.getPath().contains(str)) {
                return HttpParameterType.URI_PARAMETER;
            }
        }
        return httpParameterType;
    }

    /**
     * 初始化请求Path参数
     *
     * @param httpApi
     */
    private void initPathParameters(HttpApi httpApi) {
        List<Parameter> parameters = httpApi.getPathParameters();
        if (parameters == null) {
            parameters = new ArrayList<>();
            httpApi.setPathParameters(parameters);
        }
    }

    /**
     * 获取请求Path参数的名称
     *
     * @param ctAnnotations
     * @return
     */
    private String getPathParameterName(List<CtAnnotation<?>> ctAnnotations) {
        for (CtAnnotation<?> ctAnnotation : ctAnnotations) {
            String name = ctAnnotation.getType().getQualifiedName();
            if (name.equals(PATH_VARIABLE)) {
                String value = ctAnnotation.getValue("value").getValueByRole(CtRole.VALUE);
                if ("".equals(value)) {
                    value = ctAnnotation.getValue("name").getValueByRole(CtRole.VALUE);
                }
                return value;
            }
        }
        return "";
    }

    /**
     * 初始化请求URI参数
     *
     * @param httpApi
     */
    private void initUriParameters(HttpApi httpApi) {
        List<Parameter> parameters = httpApi.getUriParameters();
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
     * 收集Response参数
     *
     * @param apiProject
     * @param sourceProject
     * @param httpApi
     * @param ctMethod
     */
    private void collectResponseParameter(ApiProject apiProject, SourceProject sourceProject,
                                          HttpApi httpApi, CtMethod<?> ctMethod) {
        initHttpResponse(httpApi);
        Parameter parameter = buildParameter(apiProject, sourceProject,
                httpApi, null, ParameterPosition.METHOD_RETURN, ctMethod);
        httpApi.getHttpResponse().getBody().add(parameter);
    }

    /**
     * 初始化返回Body参数
     *
     * @param httpApi
     */
    private void initHttpResponse(HttpApi httpApi) {
        HttpResponse httpResponse = httpApi.getHttpResponse();
        if (httpResponse == null) {
            httpResponse = new HttpResponse();
            httpResponse.setStatus(HttpStatus.SC_OK);
            httpApi.setHttpResponse(httpResponse);
            httpResponse.setBody(new ArrayList<>());
        } else if (httpResponse.getBody() == null) {
            httpResponse.setBody(new ArrayList<>());
        }
    }

    /**
     * 构建参数
     *
     * @param apiProject
     * @param sourceProject
     * @param httpApi
     * @param parentParameter
     * @param parameterPosition
     * @param ctElement
     * @return
     */
    private Parameter buildParameter(ApiProject apiProject, SourceProject sourceProject,
                                     HttpApi httpApi, Parameter parentParameter,
                                     ParameterPosition parameterPosition, CtElement ctElement) {
        String parentKey = parentParameter != null ? parentParameter.getId() : httpApi.getId();
        String parameterKey = CtModelHelper.getParameterKey(parentKey, parameterPosition,
                (CtNamedElement) ctElement);
        String parameterName = CtModelHelper.getParameterName(parameterPosition,
                (CtNamedElement) ctElement);
        CtTypeReference<?> ctTypeReference = getCtTypeReference(parameterPosition, ctElement);
        List<CtAnnotation<?>> ctAnnotations = ctElement.getAnnotations();
        Parameter parameter = buildParameter(parentParameter, parameterKey, parameterName,
                ctTypeReference, ctAnnotations);

        apiProject.putParameter(parameterKey, parameter);
        sourceProject.putCtElement(parameterKey, ctElement);

        if (isChildBuilt(parameter)) {
            return parameter;
        }
        buildChildParameters(apiProject, sourceProject, httpApi, ctElement, parameter, ctTypeReference);
        return parameter;
    }

    /**
     * 获取参数的反射模型
     *
     * @param parameterPosition
     * @param ctElement
     * @return
     */
    private CtTypeReference<?> getCtTypeReference(ParameterPosition parameterPosition, CtElement ctElement) {
        CtTypeReference<?> ctTypeReference;
        if (parameterPosition.equals(ParameterPosition.ARRAY_SUB)) {
            CtTypeReference<?> type = ((CtTypedElement<?>) ctElement).getType();
            if (type instanceof CtArrayTypeReferenceImpl) {
                ctTypeReference = ((CtArrayTypeReferenceImpl) type).getArrayType();
            } else {
                ctTypeReference = type.getActualTypeArguments().get(0);
            }
        } else {
            ctTypeReference = ((CtTypedElement<?>) ctElement).getType();
        }
        return ctTypeReference;
    }

    /**
     * 构建参数
     *
     * @param parentParameter
     * @param parameterKey
     * @param parameterName
     * @param ctTypeReference
     * @param ctAnnotations
     * @return
     */
    private Parameter buildParameter(Parameter parentParameter, String parameterKey,
                                     String parameterName, CtTypeReference<?> ctTypeReference,
                                     List<CtAnnotation<?>> ctAnnotations) {
        Parameter parameter = new Parameter();
        parameter.setParentParameter(parentParameter);
        parameter.setId(parameterKey);
        parameter.setName(parameterName);
        ParameterType type = parameterTypeHandlerChain.handle(ctTypeReference);
        parameter.setType(type);
        parameter.setOriginType(ctTypeReference.getQualifiedName());
        parameter.setRequired(isRequired(ctAnnotations));
        collectParameterDescription(parameter, ctAnnotations);
        return parameter;
    }

    /**
     * 判断参数是否是必须的
     *
     * @param ctAnnotations
     * @return
     */
    private boolean isRequired(List<CtAnnotation<?>> ctAnnotations) {
        if (ctAnnotations == null) {
            return false;
        }

        for (CtAnnotation<?> ctAnnotation : ctAnnotations) {
            String qualifiedName = ctAnnotation.getType().getQualifiedName();
            if (qualifiedName.equals(PATH_VARIABLE) || qualifiedName.equals(REQUEST_PARAM)
                    || qualifiedName.equals(REQUEST_BODY)) {
                return ctAnnotation.getValue("required").getValueByRole(CtRole.VALUE);
            }

            String simpleName = ctAnnotation.getType().getSimpleName();
            if (simpleName.equals(NOT_NULL) || simpleName.equals(NOT_BLANK)
                    || simpleName.equals(NOT_EMPTY)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 收集参数描述
     *
     * @param parameter
     * @param ctAnnotations
     */
    private void collectParameterDescription(Parameter parameter, List<CtAnnotation<?>> ctAnnotations) {
        if (ctAnnotations == null) {
            return;
        }

        for (CtAnnotation<?> ctAnnotation : ctAnnotations) {
            String qualifiedName = ctAnnotation.getType().getQualifiedName();
            String desc = parameter.getDescription();
            if (qualifiedName.startsWith(JAVAX_VALIDATION)
                    || qualifiedName.startsWith(HIBERNATE_VALIDATOR)) {
                // TODO parse message
                String str = ctAnnotation.getValue("message").getValueByRole(CtRole.VALUE);
                desc = appendDescription(desc, str);
            }
            parameter.setDescription(desc);
        }
    }

    /**
     * 处理参数描述
     *
     * @param desc
     * @param str
     * @return
     */
    private String appendDescription(String desc, String str) {
        if (desc == null) {
            desc = str;
        } else {
            desc = String.format("%s; %s", desc, str);
        }
        return desc;
    }

    /**
     * 子参数是否已经构建过
     * 如果父参数跟本参数均是Object类型，且父参数的子参数已构建过，则不继续往下构建，否则会出现无穷无尽的向下构建
     *
     * @param parameter
     * @return
     */
    private boolean isChildBuilt(Parameter parameter) {
        Parameter parentParameter = parameter.getParentParameter();
        while (parentParameter != null) {
            if (parameter.getType() == ParameterType.OBJECT
                    && parameter.getOriginType().equals(parentParameter.getOriginType())) {
                return true;
            }
            parentParameter = parentParameter.getParentParameter();
        }
        return false;
    }

    /**
     * 构建子参数
     *
     * @param apiProject
     * @param sourceProject
     * @param httpApi
     * @param ctElement
     * @param parameter
     * @param ctTypeReference
     */
    private void buildChildParameters(ApiProject apiProject, SourceProject sourceProject,
                                      HttpApi httpApi, CtElement ctElement,
                                      Parameter parameter, CtTypeReference<?> ctTypeReference) {
        ParameterType type = parameter.getType();
        if (type == ParameterType.ARRAY) {
            List<Parameter> childParameters = new ArrayList<>();
            Parameter childParameter = buildParameter(apiProject, sourceProject,
                    httpApi, parameter, ParameterPosition.ARRAY_SUB, ctElement);
            childParameters.add(childParameter);
            parameter.setChildParameters(childParameters);
        } else if (type == ParameterType.OBJECT) {
            Collection<CtFieldReference<?>> ctFieldReferences = ctTypeReference.getDeclaredFields();
            List<Parameter> childParameters = new ArrayList<>();
            for (CtFieldReference<?> ctFieldReference : ctFieldReferences) {
                CtField<?> ctField = ctFieldReference.getFieldDeclaration();
                if (isChildParameter(ctField)) {
                    Parameter childParameter = buildParameter(apiProject, sourceProject,
                            httpApi, parameter, ParameterPosition.NORMAL, ctField);
                    childParameters.add(childParameter);
                }
            }
            parameter.setChildParameters(childParameters);
        }
    }

    /**
     * 是否是子参数：没有修饰符或只有一个修饰符（限定为public、protected、private几个）的参数才为子参数
     *
     * @param ctField
     * @return
     */
    private boolean isChildParameter(CtField<?> ctField) {
        if (ctField.getModifiers().size() == 0) {
            return true;
        } else if (ctField.getModifiers().size() == 1) {
            ModifierKind modifierKind = ctField.getModifiers().iterator().next();
            if (modifierKind.toString().equals(PUBLIC) || modifierKind.toString().equals(PROTECTED)
                    || modifierKind.toString().equals(PRIVATE)) {
                return true;
            }
        }
        return false;
    }

}
