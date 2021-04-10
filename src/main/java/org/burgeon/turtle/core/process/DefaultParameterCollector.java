package org.burgeon.turtle.core.process;

import org.burgeon.turtle.core.common.CtModelHelper;
import org.burgeon.turtle.core.model.api.*;
import org.burgeon.turtle.core.model.source.ParameterPosition;
import org.burgeon.turtle.core.model.source.SourceProject;
import org.burgeon.turtle.core.utils.StringUtils;
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
        exclusions.add("org.springframework.ui.**");
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

        HttpParameterType httpParameterType = getHttpParameterType(ctParameter.getAnnotations());
        Parameter parameter;
        switch (httpParameterType) {
            case PATH_PARAMETER:
                parameter = buildParameter(apiProject, sourceProject,
                        httpApi, null, ctParameter, HttpParameterPosition.PATH,
                        ParameterPosition.NORMAL);
                String pathParameterName = getPathParameterName(ctParameter.getAnnotations());
                if (StringUtils.notBlank(pathParameterName)) {
                    parameter.setName(pathParameterName);
                }
                initPathParameters(httpApi);
                httpApi.getPathParameters().add(parameter);
                break;
            case URI_PARAMETER:
                parameter = buildParameter(apiProject, sourceProject,
                        httpApi, null, ctParameter, HttpParameterPosition.URI,
                        ParameterPosition.NORMAL);
                initUriParameters(httpApi);
                httpApi.getUriParameters().add(parameter);
                break;
            case BODY_PARAMETER:
                parameter = buildParameter(apiProject, sourceProject,
                        httpApi, null, ctParameter, HttpParameterPosition.REQUEST,
                        ParameterPosition.NORMAL);
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
     * @param ctAnnotations
     * @return
     * @see HttpParameterType
     */
    private HttpParameterType getHttpParameterType(List<CtAnnotation<?>> ctAnnotations) {
        boolean nullPositionMark = ctAnnotations == null || (ctAnnotations.size() == 1
                && VALID.equals(ctAnnotations.get(0).getType().getQualifiedName()));
        if (nullPositionMark) {
            return HttpParameterType.URI_PARAMETER;
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
        return HttpParameterType.URI_PARAMETER;
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
            if (PATH_VARIABLE.equals(name)) {
                String value = ctAnnotation.getValue("value").getValueByRole(CtRole.VALUE);
                if (StringUtils.isBlank(value)) {
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
        Parameter parameter = buildParameter(apiProject, sourceProject,
                httpApi, null, ctMethod, HttpParameterPosition.RESPONSE,
                ParameterPosition.METHOD_RETURN);
        initHttpResponse(httpApi);
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
     * @param ctElement
     * @param httpParameterPosition
     * @param parameterPosition
     * @return
     */
    private Parameter buildParameter(ApiProject apiProject, SourceProject sourceProject,
                                     HttpApi httpApi, Parameter parentParameter, CtElement ctElement,
                                     HttpParameterPosition httpParameterPosition,
                                     ParameterPosition parameterPosition) {
        String parentKey = parentParameter != null ? parentParameter.getId() : httpApi.getId();
        String parameterKey = CtModelHelper.getParameterKey(parentKey, parameterPosition,
                (CtNamedElement) ctElement);
        String parameterName = CtModelHelper.getParameterName(parameterPosition,
                (CtNamedElement) ctElement);
        CtTypeReference<?> ctTypeReference = getCtTypeReference(parameterPosition, ctElement);
        List<CtAnnotation<?>> ctAnnotations = ctElement.getAnnotations();
        Parameter parameter = buildParameter(parentParameter, httpParameterPosition, parameterKey, parameterName,
                ctTypeReference, ctAnnotations);

        apiProject.putParameter(parameterKey, parameter);
        sourceProject.putCtElement(parameterKey, ctElement);

        if (!needBuildChild(parameter)) {
            return parameter;
        }
        buildChildParameters(apiProject, sourceProject, httpApi, parameter, ctElement, ctTypeReference,
                httpParameterPosition);
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
        if (ParameterPosition.ARRAY_SUB.equals(parameterPosition)) {
            CtTypeReference<?> type = ((CtTypedElement<?>) ctElement).getType();
            if (type instanceof CtArrayTypeReferenceImpl) {
                ctTypeReference = ((CtArrayTypeReferenceImpl) type).getArrayType();
            } else if (type.getActualTypeArguments().size() == 1) {
                ctTypeReference = type.getActualTypeArguments().get(0);
            } else {
                ctTypeReference = CtModelHelper.getObjectReference();
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
     * @param httpParameterPosition
     * @param parameterKey
     * @param parameterName
     * @param ctTypeReference
     * @param ctAnnotations
     * @return
     */
    private Parameter buildParameter(Parameter parentParameter, HttpParameterPosition httpParameterPosition,
                                     String parameterKey, String parameterName,
                                     CtTypeReference<?> ctTypeReference, List<CtAnnotation<?>> ctAnnotations) {
        Parameter parameter = new Parameter();
        parameter.setParentParameter(parentParameter);
        parameter.setId(parameterKey);
        parameter.setName(parameterName);
        parameter.setOriginType(ctTypeReference.getQualifiedName());
        ParameterType type = parameterTypeHandlerChain.handle(ctTypeReference);
        parameter.setType(type);
        parameter.setPosition(httpParameterPosition);
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
            if (PATH_VARIABLE.equals(qualifiedName) || REQUEST_PARAM.equals(qualifiedName)
                    || REQUEST_BODY.equals(qualifiedName)) {
                return ctAnnotation.getValue("required").getValueByRole(CtRole.VALUE);
            }

            String simpleName = ctAnnotation.getType().getSimpleName();
            if (NOT_NULL.equals(simpleName) || NOT_BLANK.equals(simpleName)
                    || NOT_EMPTY.equals(simpleName)) {
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
     * 子参数是否需要构建
     * 如果本参数是Object类型，且源类型跟父参数的源类型是相同的类型，且父子参数均已构建过，则不继续往下构建，否则会出现无穷无尽的向下构建
     *
     * @param parameter
     * @return
     */
    private boolean needBuildChild(Parameter parameter) {
        int buildCount = 0;
        if (parameter.getType() == ParameterType.OBJECT) {
            Parameter parentParameter = parameter.getParentParameter();
            while (parentParameter != null) {
                if (parameter.getOriginType().equals(parentParameter.getOriginType())) {
                    buildCount++;
                }
                if (buildCount > 1) {
                    return false;
                }
                parentParameter = parentParameter.getParentParameter();
            }
        }
        return true;
    }

    /**
     * 构建子参数
     *
     * @param apiProject
     * @param sourceProject
     * @param httpApi
     * @param parentParameter
     * @param parentCtElement
     * @param parentCtTypeReference
     * @param httpParameterPosition
     */
    private void buildChildParameters(ApiProject apiProject, SourceProject sourceProject, HttpApi httpApi,
                                      Parameter parentParameter, CtElement parentCtElement,
                                      CtTypeReference<?> parentCtTypeReference,
                                      HttpParameterPosition httpParameterPosition) {
        ParameterType parentType = parentParameter.getType();
        if (parentType == ParameterType.ARRAY) {
            List<Parameter> childParameters = new ArrayList<>();
            Parameter childParameter = buildParameter(apiProject, sourceProject,
                    httpApi, parentParameter, parentCtElement, httpParameterPosition, ParameterPosition.ARRAY_SUB);
            childParameters.add(childParameter);
            parentParameter.setChildParameters(childParameters);
        } else if (parentType == ParameterType.OBJECT) {
            Collection<CtFieldReference<?>> ctFieldReferences = parentCtTypeReference.getDeclaredFields();
            List<Parameter> childParameters = new ArrayList<>();
            for (CtFieldReference<?> ctFieldReference : ctFieldReferences) {
                CtField<?> ctField = ctFieldReference.getFieldDeclaration();
                if (needCollect(ctField)) {
                    Parameter childParameter = buildParameter(apiProject, sourceProject,
                            httpApi, parentParameter, ctField, httpParameterPosition, ParameterPosition.NORMAL);
                    childParameters.add(childParameter);
                }
            }
            parentParameter.setChildParameters(childParameters);
        }
    }

    /**
     * 是否需要收集：没有修饰符或只有一个修饰符（限定为public、protected、private几个）的参数才需要收集
     *
     * @param ctField
     * @return
     */
    private boolean needCollect(CtField<?> ctField) {
        if (ctField.getModifiers().isEmpty()) {
            return true;
        } else if (ctField.getModifiers().size() == 1) {
            ModifierKind modifierKind = ctField.getModifiers().iterator().next();
            if (PUBLIC.equals(modifierKind.toString()) || PROTECTED.equals(modifierKind.toString())
                    || PRIVATE.equals(modifierKind.toString())) {
                return true;
            }
        }
        return false;
    }

}
